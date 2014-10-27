package objectbackuprestore;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.Adler32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.GZIPOutputStream;

import mxmodelreflection.proxies.MxObjectMember;
import mxmodelreflection.proxies.MxObjectType;
import objectbackuprestore.data.BackupDataTerminator;
import objectbackuprestore.data.BackupObject;
import objectbackuprestore.data.BackupObjectAssociation;
import objectbackuprestore.data.BackupObjectAttribute;
import objectbackuprestore.data.BackupObjectFileDocumentContents;
import objectbackuprestore.data.BackupObjectMain;
import objectbackuprestore.data.ReferencedObjectId;
import objectbackuprestore.proxies.Backup;
import objectbackuprestore.proxies.BackupConfiguration;
import objectbackuprestore.proxies.BackupConfigurationObject;
import objectbackuprestore.proxies.BackupGroup;
import objectbackuprestore.proxies.BackupRequest;
import objectbackuprestore.proxies.IncludeInBackup;
import objectbackuprestore.proxies.ObjectType;

import org.apache.commons.io.IOUtils;

import com.mendix.core.Core;
import com.mendix.core.CoreException;
import com.mendix.logging.ILogNode;
import com.mendix.systemwideinterfaces.core.IContext;
import com.mendix.systemwideinterfaces.core.IMendixIdentifier;
import com.mendix.systemwideinterfaces.core.IMendixObject;
import com.mendix.systemwideinterfaces.core.meta.IMetaAssociation;
import com.mendix.systemwideinterfaces.core.meta.IMetaAssociation.AssociationOwner;
import com.mendix.systemwideinterfaces.core.meta.IMetaAssociation.AssociationType;
import com.mendix.systemwideinterfaces.core.meta.IMetaObject;
import com.mendix.systemwideinterfaces.core.meta.IMetaPrimitive;

/**
 * @author marcel
 * Backup a Mendix object
 */
public class BackupObjectImpl {
	
	private final String CLASS_NAME = getClass().getSimpleName();	
	private ILogNode logger;
	private IContext context;
	private BackupRequest backupRequest;
	private BackupConfiguration backupConfiguration;
	private Map<String, BackupConfigurationObject> configurationObjectMap = new HashMap<>();
	private Set<String> processedObjectSet = new HashSet<>();
	private Map<String, String> primaryKeyMap = new HashMap<>();
	private Set<String> imageAttributeToIgnoreSet = new HashSet<>();
	private Set<String> fileDocumentAttributeToIgnoreSet = new HashSet<>();
	private ByteArrayOutputStream zipBAOS;
	private CheckedOutputStream checksum;
	private GZIPOutputStream zipOS;
	private ObjectOutputStream objectOS;
	private int objectCount;
	private int referencedObjectCount;
	private int fileDocumentCount;

	/**
	 * Constructor
	 * 
	 * @param logNode
	 * @param context
	 */
	public BackupObjectImpl(String logNode, IContext context) {
		logger = Core.getLogger(logNode);
		// Use sudo context to allow full access
		this.context = context.getSudoContext();
		imageAttributeToIgnoreSet.add(BackupRestoreConstants.ATTR_PUBLIC_THUMBNAIL_PATH);
		fileDocumentAttributeToIgnoreSet.add(BackupRestoreConstants.ATTR_FILE_NAME);
		fileDocumentAttributeToIgnoreSet.add(BackupRestoreConstants.ATTR_FILE_ID);
		fileDocumentAttributeToIgnoreSet.add(BackupRestoreConstants.ATTR_CONTENTS);
		fileDocumentAttributeToIgnoreSet.add(BackupRestoreConstants.ATTR_HAS_CONTENTS);
	}
	
	/**
	 * Backup a Mendix object
	 * @param backupRequest 
	 * @return The backup data, Mendix type ObjectBackupRestore.Backup
	 */
	public IMendixObject runBackup(BackupRequest backupRequest) {
		String logPrefix = CLASS_NAME + ".runBackup ";
		if (logger.isTraceEnabled()) {
			logger.trace(logPrefix + "start");
		}
		try {
			this.backupRequest = backupRequest;
			this.backupConfiguration = backupRequest.getRunBackup_BackupConfiguration();
			initialize();

			// Backup main object(s)
			if (logger.isDebugEnabled()) {
				logger.debug(logPrefix + "Backup main object(s)");
			}
			objectCount = 0;
			MxObjectType entity = backupConfiguration.getBackupConfiguration_MxObjectType();
			String xpathQuery = "//" + entity.getCompleteName();
			String xPathConstraint = backupRequest.getXPathConstraint();
			if (xPathConstraint != null && !xPathConstraint.trim().isEmpty()) {
				xpathQuery += xPathConstraint;				
			}
			List<IMendixObject> mainObjectList = Core.retrieveXPathQuery(context, xpathQuery);
			for (IMendixObject object : mainObjectList) {
				processObject(object, "", null, true);				
			}
			if (logger.isDebugEnabled()) {
				logger.debug(logPrefix + "Include " + objectCount +" object(s) in the backup");
			}			
			
			Backup backup = createBackupDocument();
			
			if (logger.isTraceEnabled()) {
				logger.trace(logPrefix + "end");
			}
			return backup.getMendixObject();
		} catch (Exception e) {
			try {
				objectOS.close();
			} catch (Exception e2) {
				logger.error(e2);
			} 
			throw new RuntimeException(e);
		}
	}
	
	private void initialize() throws IOException, CoreException {
		String logPrefix = CLASS_NAME + ".initialize ";
		if (logger.isTraceEnabled()) {
			logger.trace(logPrefix + "start");
		}
		
		objectCount = 0;
		referencedObjectCount = 0;
		fileDocumentCount = 0;
		
		// The GZIP stream to write objects to.
		zipBAOS = new ByteArrayOutputStream();
		checksum = new CheckedOutputStream(zipBAOS, new Adler32());
		zipOS = new GZIPOutputStream(checksum);
		objectOS = new ObjectOutputStream(zipOS);
		
		configurationObjectMap.clear();
		String xpathConstraint = "[ObjectBackupRestore.BackupConfigurationObject_BackupConfiguration=" + backupConfiguration.getMendixObject().getId().toLong() + "]";
		for (IMendixObject obj : Core.retrieveXPathQuery(context, "//ObjectBackupRestore.BackupConfigurationObject" + xpathConstraint)) {
			BackupConfigurationObject backupConfigurationObject = objectbackuprestore.proxies.BackupConfigurationObject.initialize(context, obj);
			configurationObjectMap.put(backupConfigurationObject.getPath(), backupConfigurationObject);
		}
		
		processedObjectSet.clear();
				
		if (logger.isTraceEnabled()) {
			logger.trace(logPrefix + "end");
		}
	}
	
	private Backup createBackupDocument() throws IOException, CoreException {
		String logPrefix = CLASS_NAME + ".createBackupDocument ";

		Date currentDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");

		BackupGroup group = backupConfiguration.getBackupConfiguration_BackupGroup();

		if (logger.isDebugEnabled()) {
			logger.debug(logPrefix + "Write terminator object with counts");
		}
		BackupDataTerminator terminator = new BackupDataTerminator();
		terminator.setCreationDateTime(currentDate);
		terminator.setObjectCount(objectCount);
		terminator.setReferencedObjectCount(referencedObjectCount);
		terminator.setFileDocumentCount(fileDocumentCount);
		terminator.setGroupCode(group.getCode());
		terminator.setDescription(backupConfiguration.getDescription());
				
		objectOS.writeObject(terminator);
		
		if (logger.isDebugEnabled()) {
			logger.debug(logPrefix + "Create backup document");
		}
		String backupName = backupRequest.getName() + "_" + sdf.format(currentDate) + ".zip";
		Backup backupDocument = new Backup(context);
		backupDocument.setName(backupName);
		backupDocument.setBackup_BackupConfiguration(backupConfiguration);
		backupDocument.setBackup_BackupGroup(group);
		backupDocument.setGroupCode(group.getCode());
		backupDocument.setCreationDateTime(currentDate);
		backupDocument.setObjectCount(objectCount);
		backupDocument.setReferencedObjectCount(referencedObjectCount);
		backupDocument.setFileDocumentCount(fileDocumentCount);
		backupDocument.setDescription(backupConfiguration.getDescription());
		backupDocument.setImported(false);
		
		if (logger.isDebugEnabled()) {
			logger.debug(logPrefix + "Store the compressed content in the backup file document");
		}
		objectOS.close();
		byte[] backupDocumentData = zipBAOS.toByteArray();
		InputStream inputStream = new ByteArrayInputStream(backupDocumentData);
		Core.storeFileDocumentContent(context, backupDocument.getMendixObject(), inputStream);
		backupDocument.setChecksum(checksum.getChecksum().getValue());
		
		backupDocument.commit();
		
		if (logger.isTraceEnabled()) {
			logger.trace(logPrefix + "end");
		}
		return backupDocument;
	}
	
	private void processObject(IMendixObject object, String path, BackupObjectAssociation reverseReferenceParent, boolean isMainObject) throws IOException, CoreException {
		String logPrefix = CLASS_NAME + ".processObject ";
		if (logger.isTraceEnabled()) {
			logger.trace(logPrefix + "start");
		}		

		// Get the meta data object
		IMetaObject metaObject = object.getMetaObject();
		
		if (processedObjectSet.contains(getProcessedObjectSetKey(object, metaObject))) {
			logger.debug(logPrefix + "Object " + metaObject.getName() + " has already been included in the backup");
			return;
		}		
		
		// Create the backup object
		BackupObject backupObject;
		if (isMainObject) {
			objectCount += 1;
			backupObject = new BackupObjectMain();
		} else {
			referencedObjectCount += 1;
			backupObject = new BackupObject();
		}
		backupObject.setCompleteObjectName(metaObject.getName());
		backupObject.setId(object.getId().toLong());
		String keyAttributeName = getPrimaryKeyByEntityName(backupObject.getCompleteObjectName());
		if (keyAttributeName == null) {
			backupObject.setKeyAttribute(null);
		} else {
			BackupObjectAttribute keyAttribute = new BackupObjectAttribute();
			keyAttribute.setAttributeName(keyAttributeName);
			Serializable value = object.getValue(context, keyAttributeName);
			keyAttribute.setAttributeValue(value);
			backupObject.setKeyAttribute(keyAttribute);
		}

		// Persistent parent associations
		// Parent association example: The customer on an order
		if (logger.isDebugEnabled()) {
			logger.debug(logPrefix + "Process parent associations of " + backupObject.getCompleteObjectName() + ", id: " + backupObject.getId());
		}
		Collection<? extends IMetaAssociation> metaAssociationsParent = metaObject.getMetaAssociationsParent();
		processAssociations(path, object, metaObject, reverseReferenceParent, metaAssociationsParent, true, backupObject);

		// Persistent child associations
		// Child association example: all order lines related to an order. Each order line has a reference to the order.
		if (logger.isDebugEnabled()) {
			logger.debug(logPrefix + "Process child associations of " + backupObject.getCompleteObjectName() + ", id: " + backupObject.getId());
		}
		Collection<? extends IMetaAssociation> metaAssociationsChild = metaObject.getMetaAssociationsChild();
		// Self references are included in both sets, must be processed only once.
		metaAssociationsChild.removeAll(metaAssociationsParent);
		processAssociations(path, object, metaObject, reverseReferenceParent, metaAssociationsChild, false, backupObject);

		boolean isImage = Core.isSubClassOf(BackupRestoreConstants.ENTITY_SYSTEM_IMAGE, object.getType());
		boolean isFileDocument = Core.isSubClassOf(BackupRestoreConstants.ENTITY_SYSTEM_FILE_DOCUMENT, object.getType());
		
		// Attributes
		if (logger.isDebugEnabled()) {
			logger.debug(logPrefix + "Process attributes of " + backupObject.getCompleteObjectName() + ", id: " + backupObject.getId());
		}		
		for (IMetaPrimitive primitive : metaObject.getMetaPrimitives()) {
			String attributeName = primitive.getName();
			boolean includeAttribute = true;
			
			if (attributeName.equals(keyAttributeName)) {
				// Key attribute is already included.
				includeAttribute = false;
			} else if (isImage && imageAttributeToIgnoreSet.contains(attributeName)) {
				// Ignore system supplied attributes for images
				includeAttribute = false;
			} else if (isFileDocument && fileDocumentAttributeToIgnoreSet.contains(attributeName)) {
				// Ignore system supplied attributes for file documents
				includeAttribute = false;
			} else if (attributeName.equals(BackupRestoreConstants.ATTR_CREATED_DATE) && metaObject.hasCreatedDateAttr()) {
				// Ignore created date when object has it.
				// (The developer could have turned off the flag and created an attribute with the same name.)
				includeAttribute = false;
			} else if (attributeName.equals(BackupRestoreConstants.ATTR_CHANGED_DATE) && metaObject.hasChangedDateAttr()) {
				// Ignore changed date when object has it.
				// (The developer could have turned off the flag and created an attribute with the same name.)
				includeAttribute = false;
			}
			// If still selected, include the attribute
			if (includeAttribute) {
				BackupObjectAttribute backupObjectAttribute = new BackupObjectAttribute();
				backupObjectAttribute.setAttributeName(attributeName);
				Serializable value = object.getValue(context, attributeName);
				backupObjectAttribute.setAttributeValue(value);
				backupObject.getAttributeList().add(backupObjectAttribute);
			}
		}

		// Write the object to the backup
		if (logger.isDebugEnabled()) {
			logger.debug(logPrefix + "Write object " + backupObject.getCompleteObjectName() + ", id: " + backupObject.getId());
		}		
		objectOS.writeObject(backupObject);
		
		// Image or FileDocument contents
		if (isFileDocument) {
			fileDocumentCount += 1;
			Boolean hasContents = object.getValue(context, BackupRestoreConstants.ATTR_HAS_CONTENTS);
			BackupObjectFileDocumentContents fileDocumentContents = new BackupObjectFileDocumentContents();
			fileDocumentContents.setHasContents(hasContents);
			if (hasContents) {
				fileDocumentContents.setContents(IOUtils.toByteArray(Core.getFileDocumentContent(context, object)));
			}
			objectOS.writeObject(fileDocumentContents);
		}
		
		processedObjectSet.add(getProcessedObjectSetKey(object, metaObject));
		
		if (logger.isTraceEnabled()) {
			logger.trace(logPrefix + "end");
		}
	}
	
	private void processAssociations(String path, IMendixObject object, IMetaObject metaObject, BackupObjectAssociation reverseReferenceParent, Collection<? extends IMetaAssociation> metaAssociations, boolean isParentAssociation, BackupObject backupObject) throws CoreException, IOException {
		String logPrefix = CLASS_NAME + ".processAssociations ";
		if (logger.isTraceEnabled()) {
			logger.trace(logPrefix + "start");
		}
		
		for (IMetaAssociation association : metaAssociations) {
			String name = association.getName();
			String module = name.substring(0, name.indexOf(".")); 
			// Skip associations of the System module (The owner and changedBy associations are created in the System module, these cannot be processed directly)
			// Only select associations between persistable entities
			if (!module.equals(BackupRestoreConstants.MODULE_SYSTEM) && association.getParent().isPersistable() && association.getChild().isPersistable()) {
				processAssociation(path, object, metaObject, reverseReferenceParent, association, isParentAssociation, backupObject);
			}
		}
		
		if (logger.isTraceEnabled()) {
			logger.trace(logPrefix + "end");
		}
	}
	
	private void processAssociation(String path, IMendixObject object, IMetaObject metaObject, BackupObjectAssociation reverseReferenceParent, IMetaAssociation association, boolean isParentAssociation, BackupObject backupObject) throws CoreException, IOException {
		String logPrefix = CLASS_NAME + ".processAssociation ";
		if (logger.isTraceEnabled()) {
			logger.trace(logPrefix + "start");
		}
		
		IMetaObject parent = association.getParent();
		IMetaObject child = association.getChild();
		String associationName = association.getName();
		String parentName = parent.getName();
		String childName = child.getName();

		// Check reverse reference.
		if (reverseReferenceParent != null) {
			if (associationName.equals(reverseReferenceParent.getAssociationName())) {
				if (reverseReferenceParent.getReferenceIdList().isEmpty()) {
					if (logger.isTraceEnabled()) {
						logger.trace(logPrefix + "Skip the reference we came from");
					}				
				} else {
					if (logger.isTraceEnabled()) {
						logger.trace(logPrefix + "Add backup object association for the reference we came from so child reference can link to its parent ");
					}
					backupObject.getReferenceList().add(reverseReferenceParent);
				}
				return;
			}
		}
		
		String associationStartPath;
		if (path == "") {
			associationStartPath = associationName;
		} else {
			associationStartPath = path + " / " + associationName;
		}
		
		// Determine the other entity from the association.
		String referencedEntityName;
		String reverseReferenceEntityName;
		if (backupObject.getCompleteObjectName().equals(childName) || Core.isSubClassOf(childName, backupObject.getCompleteObjectName()) ) {
			referencedEntityName = parentName;
			reverseReferenceEntityName = childName;
		} else {
			referencedEntityName = childName;
			reverseReferenceEntityName = parentName;
		}			
		
		// For parent associations and child associations with owner 'both' create backup object associations
		boolean createBackupObjectAssociation = false;
		if (isParentAssociation) {
			createBackupObjectAssociation = true;
		} else {
			if (association.getOwner().equals(AssociationOwner.BOTH) || parentName.equals(childName)) {
				createBackupObjectAssociation = true;
			}
		}
		
		// Create backup object association
		BackupObjectAssociation backupObjectAssociation = null;
		if (createBackupObjectAssociation) {
			backupObjectAssociation = new BackupObjectAssociation();
			backupObjectAssociation.setAssociationName(associationName);
			backupObjectAssociation.setReferencedEntityName(referencedEntityName);					
			backupObjectAssociation.setReferenceSet(association.getType().equals(AssociationType.REFERENCESET));
			backupObject.getReferenceList().add(backupObjectAssociation);
		}

		// Get the backup configuration object for the reference.
		// This tells us the reference type.
		// When the referenced entity does not use specializations, this will be the only backup configuration object necessary.
		String associationPath = associationStartPath + " / " + referencedEntityName.substring(referencedEntityName.indexOf(".") + 1);
		BackupConfigurationObject backupConfigurationObject = configurationObjectMap.get(associationPath);					
		if (backupConfigurationObject == null) {
			throw new RuntimeException("No backup configuration object for association " + associationName + ", path: " + associationPath);
		}
		String previousReferencedObjectName = referencedEntityName;
		
		// Get the referenced objects.
		List<IMendixObject> referencedObjectList;
		// Child associations and reverse references cannot be retrieved using object.getValue as the object does not own the association
		if (!isParentAssociation) {
			// Child association: retrieve from database.
			String referencedObjectQuery = "//" + referencedEntityName + "[" + associationName + "=" + object.getId().toLong() + "]";
			if (logger.isTraceEnabled()) {
				logger.trace(logPrefix + "Retrieve reference " + associationName + " from database using " + referencedObjectQuery);
			}
			referencedObjectList = Core.retrieveXPathQuery(context, referencedObjectQuery);						
		} else {
			switch (backupConfigurationObject.getReferenceType()) {
			case Reference:
				if (logger.isTraceEnabled()) {
					logger.trace(logPrefix + "Retrieve reference " + associationName + " using object.getValue");
				}
				referencedObjectList = new ArrayList<>();
				IMendixIdentifier identifier = object.getValue(context, associationName);
				if (identifier != null) {
					referencedObjectList.add(Core.retrieveId(context, identifier));
				}
				break;

			case ReferenceSet:
				if (logger.isTraceEnabled()) {
					logger.trace(logPrefix + "Retrieve reference set " + associationName + " using object.getValue");
				}
				List<IMendixIdentifier> identifierList = object.getValue(context, associationName);
				if (identifierList == null) {
					referencedObjectList = new ArrayList<>();
				} else {
					referencedObjectList = Core.retrieveIdList(context, identifierList);
				}
				break;
				
			case ReverseReference:
				// Reverse reference: retrieve from database.
				String referencedObjectQuery = "//" + referencedEntityName + "[" + associationName + " [reversed()]=" + object.getId().toLong() + "]";
				if (logger.isTraceEnabled()) {
					logger.trace(logPrefix + "Retrieve reverse reference " + associationName + " from database using " + referencedObjectQuery);
				}
				referencedObjectList = Core.retrieveXPathQuery(context, referencedObjectQuery);						
				break;
				
			default:
				logger.warn(logPrefix + "Reference " + associationName + " not handled");
				referencedObjectList = new ArrayList<>();
				break;
			}
		}
		for (IMendixObject referencedObject : referencedObjectList) {
			// The actual object may be a specialization of the entity in the association.
			IMetaObject referencedMetaObject = referencedObject.getMetaObject();
			String referencedObjectName= referencedMetaObject.getName();

			if (!referencedObjectName.equals(previousReferencedObjectName)) {
				previousReferencedObjectName = referencedObjectName;
				if (logger.isDebugEnabled()) {
					logger.debug(logPrefix + "Get backup configuration object for association: " + associationName + ", actual referenced entity name: " + referencedObjectName);
				}
				associationPath = associationStartPath + " / " + referencedObjectName.substring(referencedObjectName.indexOf(".") + 1);
				backupConfigurationObject = configurationObjectMap.get(associationPath);					
			}
			
			if (backupConfigurationObject == null) {
				if (logger.isTraceEnabled()) {
					logger.trace(logPrefix + "No backup configuration object for association " + associationName + ", path: " + associationPath);
				}
			} else {
				// Add referenced object to backup object association.
				if (createBackupObjectAssociation) {
					// Determine the primary key name
					String primaryKeyName = getPrimaryKey(backupConfigurationObject);
					ReferencedObjectId referencedObjectId = new ReferencedObjectId();
					referencedObjectId.setObjectId(referencedObject.getId().toLong());
					if (primaryKeyName != null) {
						BackupObjectAttribute backupObjectAttribute = new BackupObjectAttribute();
						backupObjectAttribute.setAttributeName(primaryKeyName);
						Serializable value = referencedObject.getValue(context, primaryKeyName);							
						backupObjectAttribute.setAttributeValue(value);
						referencedObjectId.setKeyAttribute(backupObjectAttribute);
					}
					backupObjectAssociation.getReferenceIdList().add(referencedObjectId);
				}
				
				// Process reference that was included in the backup
				if (backupConfigurationObject.getIncludeInBackup().equals(IncludeInBackup.Yes)) {
					// Provide a reverse reference for child references so the child object can reference back.
					BackupObjectAssociation newReverseReferenceParent = new BackupObjectAssociation();
					newReverseReferenceParent.setAssociationName(associationName);
					newReverseReferenceParent.setReferencedEntityName(reverseReferenceEntityName);
					newReverseReferenceParent.setReferenceSet(association.getType().equals(AssociationType.REFERENCESET));
					// ID only for child references, parent references only need to skip the reverse reference. 
					if (!isParentAssociation) {
						ReferencedObjectId reverseReferenceId = new ReferencedObjectId();
						reverseReferenceId.setObjectId(object.getId().toLong());
						newReverseReferenceParent.getReferenceIdList().add(reverseReferenceId);
					}
					processObject(referencedObject, associationPath, newReverseReferenceParent, false);
				}
			}
		}
		
		if (logger.isTraceEnabled()) {
			logger.trace(logPrefix + "end");
		}
	}

	private String getPrimaryKey(BackupConfigurationObject backupConfigurationObject) throws CoreException {
		String logPrefix = CLASS_NAME + ".getPrimaryKey ";
		if (logger.isTraceEnabled()) {
			logger.trace(logPrefix + "start");
		}
		
		String referencedEntityCompleteName = backupConfigurationObject.getReferencedEntityCompleteName();
		String primaryKeyName;
		if (primaryKeyMap.containsKey(referencedEntityCompleteName)) {
			primaryKeyName = primaryKeyMap.get(referencedEntityCompleteName);
			if (logger.isDebugEnabled()) {
				logger.debug(logPrefix + "Retrieved primary key name " + primaryKeyName + " from map for " + referencedEntityCompleteName);
			}
		} else {
			ObjectType objectType = backupConfigurationObject.getBackupConfigurationObject_ObjectType();
			primaryKeyName = storePrimaryKey(referencedEntityCompleteName, objectType);
		}				
		if (logger.isTraceEnabled()) {
			logger.trace(logPrefix + "end");
		}
		return primaryKeyName;
	}
	
	private String getPrimaryKeyByEntityName(String entityCompleteName) throws CoreException {
		String logPrefix = CLASS_NAME + ".getPrimaryKeyByEntityName ";
		if (logger.isTraceEnabled()) {
			logger.trace(logPrefix + "start");
		}
		
		String primaryKeyName;
		if (primaryKeyMap.containsKey(entityCompleteName)) {
			primaryKeyName = primaryKeyMap.get(entityCompleteName);
			if (logger.isDebugEnabled()) {
				logger.debug(logPrefix + "Retrieved primary key name " + primaryKeyName + " from map for " + entityCompleteName);
			}
		} else {
			List<ObjectType> objectTypeList = ObjectType.load(context, "[ObjectBackupRestore.ObjectType_MxObjectType/MxModelReflection.MxObjectType/CompleteName='" + entityCompleteName + "']");
			if (objectTypeList.isEmpty()) {
				throw new RuntimeException("No ObjectType found for entity " + entityCompleteName);
			}
			primaryKeyName = storePrimaryKey(entityCompleteName, objectTypeList.get(0));
		}				
		if (logger.isTraceEnabled()) {
			logger.trace(logPrefix + "end");
		}
		return primaryKeyName;
	}

	private String storePrimaryKey(String referencedEntityCompleteName, ObjectType objectType) throws CoreException {
		String logPrefix = CLASS_NAME + ".storePrimaryKey ";
		if (logger.isTraceEnabled()) {
			logger.trace(logPrefix + "start");
		}
		MxObjectMember primaryKeyMember = objectType.getObjectType_MxObjectMember_PrimaryKey();
		String primaryKeyName;
		if (primaryKeyMember != null) {
			primaryKeyName = primaryKeyMember.getAttributeName();
			primaryKeyMap.put(referencedEntityCompleteName, primaryKeyName);
			if (logger.isDebugEnabled()) {
				logger.debug(logPrefix + "Stored primary key name " + primaryKeyName + " from database for " + referencedEntityCompleteName);
			}
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug(logPrefix + referencedEntityCompleteName + " has no primary key specified ");
			}
			primaryKeyMap.put(referencedEntityCompleteName, null);
			primaryKeyName = null;
		}
		if (logger.isTraceEnabled()) {
			logger.trace(logPrefix + "end");
		}
		return primaryKeyName;
	}
	
	private String getProcessedObjectSetKey(IMendixObject object, IMetaObject metaObject) {
		return "" + object.getId().toLong();
	}
	
}
