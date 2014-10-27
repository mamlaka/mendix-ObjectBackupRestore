package objectbackuprestore;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Adler32;
import java.util.zip.CheckedInputStream;
import java.util.zip.GZIPInputStream;

import objectbackuprestore.data.BackupDataTerminator;
import objectbackuprestore.data.BackupObject;
import objectbackuprestore.data.BackupObjectAssociation;
import objectbackuprestore.data.BackupObjectAttribute;
import objectbackuprestore.data.BackupObjectFileDocumentContents;
import objectbackuprestore.data.BackupObjectMain;
import objectbackuprestore.data.MissingReferenceItem;
import objectbackuprestore.data.ReferencedObjectId;
import objectbackuprestore.data.RestoreObjectToDoItem;
import objectbackuprestore.proxies.Backup;
import objectbackuprestore.proxies.BackupGroup;
import objectbackuprestore.proxies.RestoreActionType;
import objectbackuprestore.proxies.RestoreLog;
import objectbackuprestore.proxies.RestoreRequest;
import objectbackuprestore.proxies.TempRestoredObject;
import objectbackuprestore.proxies.TempRestoredObjectAttribute;
import objectbackuprestore.proxies.TempRestoredObjectReference;

import com.mendix.core.Core;
import com.mendix.core.CoreException;
import com.mendix.logging.ILogNode;
import com.mendix.systemwideinterfaces.core.IContext;
import com.mendix.systemwideinterfaces.core.IMendixIdentifier;
import com.mendix.systemwideinterfaces.core.IMendixObject;
import com.mendix.systemwideinterfaces.core.meta.IMetaPrimitive;

/**
 * @author marcel
 * Backup objects from a backup
 */
public class RestoreObjectsImpl {

	private final String CLASS_NAME = getClass().getSimpleName();
	private ILogNode logger;
	private IContext context;
	private RestoreRequest restoreRequest;
	private Backup backupToRestore;
	private InputStream restoreDocumentIS;
	private CheckedInputStream checksum;
	private GZIPInputStream zipIS;
	private ObjectInputStream objectIS;
	private int objectCount;
	private int referencedObjectCount;
	private int fileDocumentCount;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
	private List<RestoreObjectToDoItem> toDoList = new ArrayList<>(); // Objects with referenced that have not been resolved yet. 
	private Map<Long, IMendixIdentifier> guidMap = new HashMap<>(); // Map GUID in backup to GUID in the target database.
	private List<IMendixObject> commitList = new ArrayList<>(); // Objects to be committed.
	private String filterObjectName;

	/**
	 * Constructor
	 * 
	 * @param logNode
	 * @param context
	 */
	public RestoreObjectsImpl(String logNode, IContext context) {
		logger = Core.getLogger(logNode);
		// Use sudo context to allow full access
		this.context = context.getSudoContext();		
	}
	
	/**
	 * Backup objects from a backup
	 * @param backupToRestore
	 * @param restoreRequest 
	 * @param actionType 
	 * @return Indicator whether the operation was successful
	 */
	public boolean restoreObjects(Backup backupToRestore, RestoreRequest restoreRequest, RestoreActionType actionType) {
		String logPrefix = CLASS_NAME + ".restoreObjects ";
		if (logger.isTraceEnabled()) {
			logger.trace(logPrefix + "start");
		}
		boolean result = true;
		try {			
			this.backupToRestore = backupToRestore;
			this.restoreRequest = restoreRequest;

			initialize();
			
			logger.info(logPrefix + "Read objects from backup - start");
			
			BackupDataTerminator terminator = null;
			boolean continueLoop = true;
			while (continueLoop) {
				Object object = objectIS.readObject();
				if (object instanceof BackupObjectMain) {
					objectCount += 1;
					BackupObjectMain mainObject = (BackupObjectMain) object;
					if (logger.isDebugEnabled()) {
						logger.debug(logPrefix + "Main object: " + mainObject.getCompleteObjectName() + ":" + mainObject.getId());
					}
					switch (actionType) {
					case UpdateDatabase:
						restoreObject(mainObject);
						break;
						
					case ReadDetails:
						// When not updating the database, load the objects in the backup in temporary Mendix objects.
						createTempRestoredObject(mainObject);

					default:
						break;
					}
					
				} else if (object instanceof BackupObject) {
					referencedObjectCount += 1;
					BackupObject referencedObject = (BackupObject) object;
					if (logger.isDebugEnabled()) {
						logger.debug(logPrefix + "Object: " + referencedObject.getCompleteObjectName() + ":" + referencedObject.getId());
					}
					switch (actionType) {
					case UpdateDatabase:
						restoreObject(referencedObject);
						break;
						
					case ReadDetails:
						// When not updating the database, load the objects in the backup in temporary Mendix objects.
						createTempRestoredObject(referencedObject);

					default:
						break;
					}
					
				} else if (object instanceof BackupObjectFileDocumentContents) {
					BackupObjectFileDocumentContents fileDocumentContents = (BackupObjectFileDocumentContents) object;
					if (logger.isDebugEnabled()) {
						logger.debug(logPrefix + "File document contents of previous object, has contents: " + fileDocumentContents.isHasContents());
					}
					switch (actionType) {
					case UpdateDatabase:
						throw new RuntimeException("File document contents must be handled with the object");
						
					case ReadDetails:
						throw new RuntimeException("File document contents must be handled with the object");
						
					case ReadSummary:
						fileDocumentCount += 1;
						break;
					}
					
				} else if (object instanceof BackupDataTerminator) {
					terminator = (BackupDataTerminator) object;
					continueLoop = false;
					if (objectCount != terminator.getObjectCount() || referencedObjectCount != terminator.getReferencedObjectCount() || fileDocumentCount != terminator.getFileDocumentCount()) {
						throw new RuntimeException("Mismatch between actual object count and terminator data" + 
								", objectCount: " + objectCount + "/" + terminator.getObjectCount() +
								", referencedObjectCount: " + referencedObjectCount + "/" + terminator.getReferencedObjectCount() +
								", fileDocumentCount: " + fileDocumentCount + "/" + terminator.getFileDocumentCount()								
						);
					}
					
				} else {
					throw new RuntimeException("Object " + object.getClass().getCanonicalName() + " not supported");
				}
			}

			objectIS.close();

			logger.info(logPrefix + "Read objects from backup - complete");
			
			switch (actionType) {
			case ReadSummary:
				if (backupToRestore.getImported()) {
					logger.info(logPrefix + "Update imported backup");
					backupToRestore.setGroupCode(terminator.getGroupCode());
					List<BackupGroup> groupList = BackupGroup.load(context, "[Code='" + terminator.getGroupCode() + "']");
					if (!groupList.isEmpty()) {
						backupToRestore.setBackup_BackupGroup(groupList.get(0));
					}
					backupToRestore.setCreationDateTime(terminator.getCreationDateTime());
					backupToRestore.setDescription(terminator.getDescription());
					backupToRestore.setObjectCount(objectCount);
					backupToRestore.setReferencedObjectCount(referencedObjectCount);
					backupToRestore.setFileDocumentCount(fileDocumentCount);
					backupToRestore.setChecksum(checksum.getChecksum().getValue());
					backupToRestore.commit();
				}
				
				break;

			case UpdateDatabase:
				List<RestoreObjectToDoItem> processedItemList = new ArrayList<>();
				logger.info(logPrefix + "Resolve additional references");
				List<MissingReferenceItem> missingReferenceList = new ArrayList<>();
				for (RestoreObjectToDoItem toDoItem : toDoList) {
					String completeObjectName = toDoItem.getObject().getType();
					boolean allReferencesProcessed = true;
					for (BackupObjectAssociation association : toDoItem.getAssociationList()) {
						if (!restoreAssociation(toDoItem.getObject(), association, completeObjectName)) {
							allReferencesProcessed = false;
							MissingReferenceItem missingReferenceItem = new MissingReferenceItem();
							missingReferenceItem.setObject(toDoItem.getObject());
							missingReferenceItem.setAssociation(association);
							missingReferenceList.add(missingReferenceItem);
						}
					}
					if (allReferencesProcessed) {
						processedItemList.add(toDoItem);
						commitList.add(toDoItem.getObject());
					}
				}
				toDoList.removeAll(processedItemList);
				if (toDoList.isEmpty()) {
					logger.info(logPrefix + "Commit " + commitList.size() + " objects");
					Core.commitWithoutEvents(context, commitList);
					logger.info(logPrefix + "Commit complete");
					RestoreLog logItem = new RestoreLog(context);
					logItem.setRestoreLog_RestoreRequest(restoreRequest);
					String message = commitList.size() + " objects restored.";
					logItem.setMessage(message);
					logItem.commit();
				} else {
					result = false;
					logger.critical(logPrefix + toDoList.size() + " object(s) could not be restored because referenced objects could not be found");
					List<IMendixObject> rollbackList = new ArrayList<>(commitList);
					for (RestoreObjectToDoItem toDoItem : toDoList) {
						rollbackList.add(toDoItem.getObject());
					}
					logger.info(logPrefix + "Rollback " + rollbackList.size() + " objects");
					for (IMendixObject object : rollbackList) {
						Core.rollback(context, object);
					}
					logger.info(logPrefix + "Rollback complete");
					
					for (MissingReferenceItem missingReferenceItem : missingReferenceList) {
						BackupObjectAssociation association = missingReferenceItem.getAssociation();
						for (ReferencedObjectId referencedObjectId : association.getReferenceIdList()) {
							RestoreLog logItem = new RestoreLog(context);
							logItem.setRestoreLog_RestoreRequest(restoreRequest);
							String message = "Object " + missingReferenceItem.getObject().getType() + " id " + missingReferenceItem.getObject().getId().toLong() + 
									" refers to " + association.getReferencedEntityName() + " ";
							BackupObjectAttribute keyAttribute = referencedObjectId.getKeyAttribute();
							if (keyAttribute != null) {
								message += "using key " + keyAttribute.getAttributeName() + "=" + keyAttribute.getAttributeValue();
							}
							message += " which was not found. (" + association.getAssociationName() + ") ";
							if (association.isReferenceSet()) {
								message += " For reference sets, all IDs are listed, at least one of them is missing.";
							}
							logItem.setMessage(message);
							logItem.commit();
						}
					}
				}

			default:
				break;
			}
				
		} catch (Exception e) {
			try {
				objectIS.close();
			} catch (Exception e2) {
				logger.error(e2);
			} 
			throw new RuntimeException(e);
		}
		
		if (logger.isTraceEnabled()) {
			logger.trace(logPrefix + "end");
		}
		return result;
	}
	
	private void initialize() throws IOException, CoreException {
		String logPrefix = CLASS_NAME + ".initialize ";
		if (logger.isTraceEnabled()) {
			logger.trace(logPrefix + "start");
		}
		
		objectCount = 0;
		referencedObjectCount = 0;
		fileDocumentCount = 0;
		toDoList.clear();
		guidMap.clear();
		
		restoreDocumentIS = Core.getFileDocumentContent(context, backupToRestore.getMendixObject());
		checksum = new CheckedInputStream(restoreDocumentIS, new Adler32());
		zipIS = new GZIPInputStream(checksum);
		objectIS = new ObjectInputStream(zipIS);
		
		if (restoreRequest != null) {
			filterObjectName = restoreRequest.getFilterObjectName();
			if (filterObjectName == null) {
				filterObjectName = "";
			} else {
				filterObjectName = filterObjectName.trim();
			}
		}
		
		if (logger.isTraceEnabled()) {
			logger.trace(logPrefix + "end");
		}
	}
		
	
	private void createTempRestoredObject(BackupObject backupObject) throws CoreException, IOException, ClassNotFoundException {
		String logPrefix = CLASS_NAME + ".createTempRestoredObject ";
		if (logger.isTraceEnabled()) {
			logger.trace(logPrefix + "start");
		}

		String completeObjectName = backupObject.getCompleteObjectName();
		
		boolean isSelected = true;
		if (!filterObjectName.isEmpty()) {
			if (completeObjectName.indexOf(filterObjectName) == -1) {
				isSelected = false;
			}
		}
		if (isSelected) {
			long objectID = backupObject.getId();
			TempRestoredObject tempObj = new TempRestoredObject(context);
			tempObj.setTempRestoredObject_Backup(backupToRestore);
			tempObj.setCompleteObjectName(completeObjectName);
			tempObj.setObjectID(Long.toString(objectID));
			tempObj.setDisplayName(completeObjectName + ":" + objectID);
			tempObj.commit();
			BackupObjectAttribute keyAttribute = backupObject.getKeyAttribute();
			if (keyAttribute != null) {
				createTempRestoredAttribute(completeObjectName, tempObj, keyAttribute);
			}
			for (BackupObjectAttribute member : backupObject.getAttributeList()) {
				String memberName = member.getAttributeName();
				if (logger.isDebugEnabled()) {
					logger.debug(logPrefix + "Member: " + memberName);
				}
				createTempRestoredAttribute(completeObjectName, tempObj, member);
			}
			
			for (BackupObjectAssociation association : backupObject.getReferenceList()) {
				String associationName = association.getAssociationName();
				if (logger.isDebugEnabled()) {
					logger.debug(logPrefix + "Association: " + associationName);
				}
				for (ReferencedObjectId referencedObjectId : association.getReferenceIdList()) {
					TempRestoredObjectReference tempReference = new TempRestoredObjectReference(context);
					tempReference.setTempRestoredObjectReference_TempRestoredObject(tempObj);
					tempReference.setReferenceName(associationName);
					String referencedEntityName = association.getReferencedEntityName();
					tempReference.setReferencedEntityCompleteName(referencedEntityName);
					tempReference.setIsReferenceSet(association.isReferenceSet());
					tempReference.setObjectID(Long.toString(referencedObjectId.getObjectId()));
					BackupObjectAttribute referencedKeyAttribute = referencedObjectId.getKeyAttribute();
					if (referencedKeyAttribute != null) {
						tempReference.setObjectKeyName(referencedKeyAttribute.getAttributeName());
						tempReference.setObjectKeyValue(getMemberValue(referencedEntityName, referencedKeyAttribute.getAttributeValue(), referencedKeyAttribute.getAttributeName()));
					}
					tempReference.commit();
				}				
			}
		} else {
			if (logger.isTraceEnabled()) {
				logger.trace(logPrefix + "Object not selected");
			}
		}
		
		
		boolean isFileDocument = Core.isSubClassOf("System.FileDocument", completeObjectName);
		if (isFileDocument) {
			fileDocumentCount += 1;
			// A file document will be followed by its contents.
			BackupObjectFileDocumentContents fileDocumentContents = (BackupObjectFileDocumentContents) objectIS.readObject();
			if (logger.isDebugEnabled()) {
				logger.debug(logPrefix + "File document contents for previous object, has contents: " + fileDocumentContents.isHasContents());
			}
		}
				
		if (logger.isTraceEnabled()) {
			logger.trace(logPrefix + "end");
		}
	}

	private void createTempRestoredAttribute(String completeObjectName, TempRestoredObject tempObj, BackupObjectAttribute member) throws CoreException {
		String logPrefix = CLASS_NAME + ".createTempRestoredAttribute ";
		if (logger.isTraceEnabled()) {
			logger.trace(logPrefix + "start");
		}
		
		String memberName = member.getAttributeName();
		TempRestoredObjectAttribute tempAttribute = new TempRestoredObjectAttribute(context);
		tempAttribute.setTempRestoredObjectAttribute_TempRestoredObject(tempObj);
		tempAttribute.setAttributeName(memberName);
		String memberValue = getMemberValue(completeObjectName, member.getAttributeValue(), memberName);
		tempAttribute.setAttributeValue(memberValue);
		tempAttribute.commit();

		if (logger.isTraceEnabled()) {
			logger.trace(logPrefix + "end");
		}
	}

	private String getMemberValue(String completeObjectName, Serializable value, String memberName) {
		String memberValue = null;
		if (value != null) {
			IMetaPrimitive memberType = Core.getMetaPrimitive(completeObjectName + "." + memberName);
			switch (memberType.getType()) {
			case String:
			case Enum:
			case HashString:
				memberValue = (String) value;
				break;
				
			case Long:
			case AutoNumber:
				memberValue = ((Long) value).toString();
				break;
				
			case Boolean:
				memberValue = ((Boolean) value).toString();
				break;
				
			case Float:
			case Currency:
				memberValue = ((Double) value).toString();
				break;
				
			case DateTime:
				Date date = (Date) value;
				memberValue = sdf.format(date);
				break;
				
			case Integer:
				memberValue = ((Integer) value).toString();
				break;
	
			default:
				throw new RuntimeException("Primitive type " + memberType.getType().name() + " not supported");
			}
		}
		return memberValue;
	}
	
	private void restoreObject(BackupObject backupObject) throws CoreException, ClassNotFoundException, IOException {
		String logPrefix = CLASS_NAME + ".restoreObject ";
		if (logger.isTraceEnabled()) {
			logger.trace(logPrefix + "start");
		}

		String completeObjectName = backupObject.getCompleteObjectName();

		IMendixIdentifier existingObjectIdentifier = checkObjectExists(backupObject.getId(), backupObject.getKeyAttribute(), completeObjectName);
		if (existingObjectIdentifier != null) {
			if (logger.isTraceEnabled()) {
				logger.trace(logPrefix + "object already exists; end");
			}
			return;
		}

		// Create object
		IMendixObject object = Core.instantiate(context, completeObjectName);
		long newObjectId = object.getId().toLong();
		if (logger.isDebugEnabled()) {
			logger.debug(logPrefix + "Created object " + completeObjectName + " with ID " + newObjectId);
		}
		// Link the ID to the ID in the backup
		guidMap.put(backupObject.getId(), object.getId());
		// Set the value of the key, if any.
		BackupObjectAttribute keyAttribute = backupObject.getKeyAttribute();
		if (keyAttribute != null) {
			object.setValue(context, keyAttribute.getAttributeName(), keyAttribute.getAttributeValue());
		}

		// Attributes
		for (BackupObjectAttribute member : backupObject.getAttributeList()) {
			String memberName = member.getAttributeName();
			if (logger.isDebugEnabled()) {
				logger.debug(logPrefix + "Member: " + memberName);
			}
			object.setValue(context, memberName, member.getAttributeValue());
		}

		// Associations
		// When the referenced object is found, it will be used as value for the reference.
		// If no object was found, the association will be put on the to do list. 
		RestoreObjectToDoItem toDoItem = null;
		for (BackupObjectAssociation association : backupObject.getReferenceList()) {
			String associationName = association.getAssociationName();
			if (logger.isDebugEnabled()) {
				logger.debug(logPrefix + "Association: " + associationName);
			}
			if (!association.getReferenceIdList().isEmpty()) {
				if (!restoreAssociation(object, association, completeObjectName)) {
					toDoItem = putAssociationOnToDoListItem(toDoItem, object, association);					
				}
			}
		}

		// File document content
		boolean isFileDocument = Core.isSubClassOf(BackupRestoreConstants.ENTITY_SYSTEM_FILE_DOCUMENT, completeObjectName);
		boolean isImage = Core.isSubClassOf(BackupRestoreConstants.ENTITY_SYSTEM_IMAGE, completeObjectName);
		if (isFileDocument) {
			fileDocumentCount += 1;
			// A file document will be followed by its contents.
			BackupObjectFileDocumentContents fileDocumentContents = (BackupObjectFileDocumentContents) objectIS.readObject();
			if (logger.isDebugEnabled()) {
				logger.debug(logPrefix + "File document contents for previous object, has contents: " + fileDocumentContents.isHasContents());
			}
			if (fileDocumentContents.isHasContents()) {
				ByteArrayInputStream fileDocumentContentsIS = new ByteArrayInputStream(fileDocumentContents.getContents());
				if (isImage) {
					Core.storeImageDocumentContent(context, object, fileDocumentContentsIS, 0, 0);
				} else {
					Core.storeFileDocumentContent(context, object, fileDocumentContentsIS);
				}
			}
		}
		
		// Commit only if all references were resolved
		if (toDoItem == null) {
			// Add to commit list
			commitList.add(object);
		} else {
			toDoList.add(toDoItem);
		}
				
		if (logger.isTraceEnabled()) {
			logger.trace(logPrefix + "end");
		}
	}
	
	private IMendixIdentifier checkObjectExists(Long objectId, BackupObjectAttribute keyAttribute, String completeObjectName) throws CoreException {
		String logPrefix = CLASS_NAME + ".checkObjectExists ";
		if (logger.isTraceEnabled()) {
			logger.trace(logPrefix + "start");
		}

		// Object has already been retrieved or created?
		if (guidMap.containsKey(objectId)) {
			IMendixIdentifier mendixIdentifier = guidMap.get(objectId);
			if (logger.isDebugEnabled()) {
				logger.debug(logPrefix + "Object " + completeObjectName + ", id: " + objectId + " found in GUID map with new value " + mendixIdentifier.toLong());
			}
			return mendixIdentifier;
		}
		
		// Object has not been created yet and has a functional key value 
		if (keyAttribute != null) {
			// Get key information
			String keyAttributeName = keyAttribute.getAttributeName();
			Serializable keyValue = keyAttribute.getAttributeValue();
			String keyStringValue = keyValue.toString();
			// Must use quotes for string values, this includes enums
			boolean useQuotes = (keyValue instanceof String);
			// Build XPath query
			StringBuilder xpathQuery = new StringBuilder("//");
			xpathQuery.append(completeObjectName);
			xpathQuery.append("[");
			xpathQuery.append(keyAttributeName);
			xpathQuery.append("=");
			if (useQuotes) {
				xpathQuery.append("'");					
			}
			xpathQuery.append(keyStringValue);
			if (useQuotes) {
				xpathQuery.append("'");					
			}
			xpathQuery.append("]");
			if (logger.isDebugEnabled()) {
				logger.debug(logPrefix + "Get object from database using: " + xpathQuery);
			}
			// Get the object
			List<IMendixObject> existingObjectList = Core.retrieveXPathQuery(context, xpathQuery.toString());
			// Not found?
			if (existingObjectList.isEmpty()) {
				if (logger.isDebugEnabled()) {
					logger.debug(logPrefix + "Object " + completeObjectName + " with key " + keyAttributeName + ", value: " + keyStringValue + " not found in database");
				}
				return null;					
				
			}
			// Found one?
			if (existingObjectList.size() == 1) {
				IMendixIdentifier mendixIdentifier = existingObjectList.get(0).getId();
				if (logger.isDebugEnabled()) {
					logger.debug(logPrefix + "Object " + completeObjectName + " with key " + keyAttributeName + ", value: " + keyStringValue + " found in database with GUID " + mendixIdentifier.toLong());
				}
				guidMap.put(objectId, mendixIdentifier);
				return mendixIdentifier;					
			}
			// Oops! Found too many objects! Key not unique.
			throw new RuntimeException("Search for object " + completeObjectName + " with key " + keyAttributeName + ", value: " + keyStringValue + " resulted in " + existingObjectList.size() + " results, zero or one objects were expected");
			
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug(logPrefix + "Object " + completeObjectName + " not in GUID map and has no key ");
			}
			return null;
		}
				
	}

	private boolean restoreAssociation(IMendixObject object, BackupObjectAssociation association, String completeObjectName) throws CoreException {
		String logPrefix = CLASS_NAME + ".restoreAssociation ";
		if (logger.isTraceEnabled()) {
			logger.trace(logPrefix + "start");
		}
		boolean result = true;
		String associationName = association.getAssociationName();

		if (association.isReferenceSet()) {
			List<IMendixIdentifier> referencedObjectMendixIdentifierList = new ArrayList<>();
			for (ReferencedObjectId referencedObjectId : association.getReferenceIdList()) {
				IMendixIdentifier referencedObjectMendixIdentifier = checkObjectExists(referencedObjectId.getObjectId(), referencedObjectId.getKeyAttribute(), association.getReferencedEntityName());
				if (referencedObjectMendixIdentifier == null) {
					result = false;
				} else {
					referencedObjectMendixIdentifierList.add(referencedObjectMendixIdentifier);
				}
			}
			if (result) {
				object.setValue(context, associationName, referencedObjectMendixIdentifierList);						
			}
		} else {
			ReferencedObjectId referencedObjectId = association.getReferenceIdList().get(0);
			IMendixIdentifier referencedObjectMendixIdentifier = checkObjectExists(referencedObjectId.getObjectId(), referencedObjectId.getKeyAttribute(), association.getReferencedEntityName());
			if (referencedObjectMendixIdentifier != null) {
				object.setValue(context, associationName, referencedObjectMendixIdentifier);
			} else {
				result = false;
			}
		}
		
		if (logger.isTraceEnabled()) {
			logger.trace(logPrefix + "end");
		}
		return result;
	}
	
	private RestoreObjectToDoItem putAssociationOnToDoListItem(RestoreObjectToDoItem restoreObjectToDoItem, IMendixObject object, BackupObjectAssociation association) {
		String logPrefix = CLASS_NAME + ".putAssociationOnToDoListItem ";
		if (logger.isTraceEnabled()) {
			logger.trace(logPrefix + "start");
		}
		
		if (restoreObjectToDoItem == null) {
			restoreObjectToDoItem = new RestoreObjectToDoItem();
			restoreObjectToDoItem.setObject(object);
		}
		restoreObjectToDoItem.getAssociationList().add(association);
		
		if (logger.isTraceEnabled()) {
			logger.trace(logPrefix + "end");
		}
		return restoreObjectToDoItem;
	}
	}
