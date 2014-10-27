// This file was generated by Mendix Business Modeler.
//
// WARNING: Code you write here will be lost the next time you deploy the project.

package objectbackuprestore.proxies;

import com.mendix.core.Core;
import com.mendix.core.CoreException;
import com.mendix.systemwideinterfaces.core.IContext;
import com.mendix.systemwideinterfaces.core.IMendixIdentifier;
import com.mendix.systemwideinterfaces.core.IMendixObject;

/**
 * 
 */
public class BackupConfigurationReferenceNodeData extends objectbackuprestore.proxies.BackupConfigurationTreeNodeData
{
	/**
	 * Internal name of this entity
	 */
	public static final String entityName = "ObjectBackupRestore.BackupConfigurationReferenceNodeData";

	/**
	 * Enum describing members of this entity
	 */
	public enum MemberNames
	{
		Caption("Caption"),
		NodeClass("NodeClass"),
		Key("Key"),
		BackupConfigurationReferenceNodeData_BackupConfigurationObject("ObjectBackupRestore.BackupConfigurationReferenceNodeData_BackupConfigurationObject"),
		BackupConfigurationReferenceNodeData_MxObjectReference("ObjectBackupRestore.BackupConfigurationReferenceNodeData_MxObjectReference"),
		BackupConfigurationReferenceNodeData_MxObjectType_From("ObjectBackupRestore.BackupConfigurationReferenceNodeData_MxObjectType_From"),
		BackupConfigurationReferenceNodeData_MxObjectType_To("ObjectBackupRestore.BackupConfigurationReferenceNodeData_MxObjectType_To"),
		BackupConfigurationObjectNodeData_BackupConfigurationTreeData("ObjectBackupRestore.BackupConfigurationObjectNodeData_BackupConfigurationTreeData"),
		TreeViewNodeData_TreeViewWidgetData("ObjectBackupRestore.TreeViewNodeData_TreeViewWidgetData"),
		ParentNode("ObjectBackupRestore.ParentNode");

		private String metaName;

		MemberNames(String s)
		{
			metaName = s;
		}

		@Override
		public String toString()
		{
			return metaName;
		}
	}

	public BackupConfigurationReferenceNodeData(IContext context)
	{
		this(context, Core.instantiate(context, "ObjectBackupRestore.BackupConfigurationReferenceNodeData"));
	}

	protected BackupConfigurationReferenceNodeData(IContext context, IMendixObject backupConfigurationReferenceNodeDataMendixObject)
	{
		super(context, backupConfigurationReferenceNodeDataMendixObject);
		if (!Core.isSubClassOf("ObjectBackupRestore.BackupConfigurationReferenceNodeData", backupConfigurationReferenceNodeDataMendixObject.getType()))
			throw new IllegalArgumentException("The given object is not a ObjectBackupRestore.BackupConfigurationReferenceNodeData");
	}

	/**
	 * @deprecated Use 'new BackupConfigurationReferenceNodeData(Context)' instead. Note that the constructor will not insert the new object in the database.
	 */
	@Deprecated
	public static objectbackuprestore.proxies.BackupConfigurationReferenceNodeData create(IContext context) throws CoreException
	{
		IMendixObject mendixObject = Core.create(context, "ObjectBackupRestore.BackupConfigurationReferenceNodeData");
		return new objectbackuprestore.proxies.BackupConfigurationReferenceNodeData(context, mendixObject);
	}

	/**
	 * @deprecated Use 'BackupConfigurationReferenceNodeData.load(IContext, IMendixIdentifier)' instead.
	 */
	@Deprecated
	public static objectbackuprestore.proxies.BackupConfigurationReferenceNodeData initialize(IContext context, IMendixIdentifier mendixIdentifier) throws CoreException
	{
		return objectbackuprestore.proxies.BackupConfigurationReferenceNodeData.load(context, mendixIdentifier);
	}

	/**
	 * Initialize a proxy using context (recommended). This context will be used for security checking when the get- and set-methods without context parameters are called.
	 * The get- and set-methods with context parameter should be used when for instance sudo access is necessary (IContext.getSudoContext() can be used to obtain sudo access).
	 */
	public static objectbackuprestore.proxies.BackupConfigurationReferenceNodeData initialize(IContext context, IMendixObject mendixObject)
	{
		return new objectbackuprestore.proxies.BackupConfigurationReferenceNodeData(context, mendixObject);
	}

	public static objectbackuprestore.proxies.BackupConfigurationReferenceNodeData load(IContext context, IMendixIdentifier mendixIdentifier) throws CoreException
	{
		IMendixObject mendixObject = Core.retrieveId(context, mendixIdentifier);
		return objectbackuprestore.proxies.BackupConfigurationReferenceNodeData.initialize(context, mendixObject);
	}

	/**
	 * @return value of BackupConfigurationReferenceNodeData_BackupConfigurationObject
	 */
	public final objectbackuprestore.proxies.BackupConfigurationObject getBackupConfigurationReferenceNodeData_BackupConfigurationObject() throws CoreException
	{
		return getBackupConfigurationReferenceNodeData_BackupConfigurationObject(getContext());
	}

	/**
	 * @param context
	 * @return value of BackupConfigurationReferenceNodeData_BackupConfigurationObject
	 */
	public final objectbackuprestore.proxies.BackupConfigurationObject getBackupConfigurationReferenceNodeData_BackupConfigurationObject(IContext context) throws CoreException
	{
		objectbackuprestore.proxies.BackupConfigurationObject result = null;
		IMendixIdentifier identifier = getMendixObject().getValue(context, MemberNames.BackupConfigurationReferenceNodeData_BackupConfigurationObject.toString());
		if (identifier != null)
			result = objectbackuprestore.proxies.BackupConfigurationObject.load(context, identifier);
		return result;
	}

	/**
	 * Set value of BackupConfigurationReferenceNodeData_BackupConfigurationObject
	 * @param backupconfigurationreferencenodedata_backupconfigurationobject
	 */
	public final void setBackupConfigurationReferenceNodeData_BackupConfigurationObject(objectbackuprestore.proxies.BackupConfigurationObject backupconfigurationreferencenodedata_backupconfigurationobject)
	{
		setBackupConfigurationReferenceNodeData_BackupConfigurationObject(getContext(), backupconfigurationreferencenodedata_backupconfigurationobject);
	}

	/**
	 * Set value of BackupConfigurationReferenceNodeData_BackupConfigurationObject
	 * @param context
	 * @param backupconfigurationreferencenodedata_backupconfigurationobject
	 */
	public final void setBackupConfigurationReferenceNodeData_BackupConfigurationObject(IContext context, objectbackuprestore.proxies.BackupConfigurationObject backupconfigurationreferencenodedata_backupconfigurationobject)
	{
		if (backupconfigurationreferencenodedata_backupconfigurationobject == null)
			getMendixObject().setValue(context, MemberNames.BackupConfigurationReferenceNodeData_BackupConfigurationObject.toString(), null);
		else
			getMendixObject().setValue(context, MemberNames.BackupConfigurationReferenceNodeData_BackupConfigurationObject.toString(), backupconfigurationreferencenodedata_backupconfigurationobject.getMendixObject().getId());
	}

	/**
	 * @return value of BackupConfigurationReferenceNodeData_MxObjectReference
	 */
	public final mxmodelreflection.proxies.MxObjectReference getBackupConfigurationReferenceNodeData_MxObjectReference() throws CoreException
	{
		return getBackupConfigurationReferenceNodeData_MxObjectReference(getContext());
	}

	/**
	 * @param context
	 * @return value of BackupConfigurationReferenceNodeData_MxObjectReference
	 */
	public final mxmodelreflection.proxies.MxObjectReference getBackupConfigurationReferenceNodeData_MxObjectReference(IContext context) throws CoreException
	{
		mxmodelreflection.proxies.MxObjectReference result = null;
		IMendixIdentifier identifier = getMendixObject().getValue(context, MemberNames.BackupConfigurationReferenceNodeData_MxObjectReference.toString());
		if (identifier != null)
			result = mxmodelreflection.proxies.MxObjectReference.load(context, identifier);
		return result;
	}

	/**
	 * Set value of BackupConfigurationReferenceNodeData_MxObjectReference
	 * @param backupconfigurationreferencenodedata_mxobjectreference
	 */
	public final void setBackupConfigurationReferenceNodeData_MxObjectReference(mxmodelreflection.proxies.MxObjectReference backupconfigurationreferencenodedata_mxobjectreference)
	{
		setBackupConfigurationReferenceNodeData_MxObjectReference(getContext(), backupconfigurationreferencenodedata_mxobjectreference);
	}

	/**
	 * Set value of BackupConfigurationReferenceNodeData_MxObjectReference
	 * @param context
	 * @param backupconfigurationreferencenodedata_mxobjectreference
	 */
	public final void setBackupConfigurationReferenceNodeData_MxObjectReference(IContext context, mxmodelreflection.proxies.MxObjectReference backupconfigurationreferencenodedata_mxobjectreference)
	{
		if (backupconfigurationreferencenodedata_mxobjectreference == null)
			getMendixObject().setValue(context, MemberNames.BackupConfigurationReferenceNodeData_MxObjectReference.toString(), null);
		else
			getMendixObject().setValue(context, MemberNames.BackupConfigurationReferenceNodeData_MxObjectReference.toString(), backupconfigurationreferencenodedata_mxobjectreference.getMendixObject().getId());
	}

	/**
	 * @return value of BackupConfigurationReferenceNodeData_MxObjectType_From
	 */
	public final mxmodelreflection.proxies.MxObjectType getBackupConfigurationReferenceNodeData_MxObjectType_From() throws CoreException
	{
		return getBackupConfigurationReferenceNodeData_MxObjectType_From(getContext());
	}

	/**
	 * @param context
	 * @return value of BackupConfigurationReferenceNodeData_MxObjectType_From
	 */
	public final mxmodelreflection.proxies.MxObjectType getBackupConfigurationReferenceNodeData_MxObjectType_From(IContext context) throws CoreException
	{
		mxmodelreflection.proxies.MxObjectType result = null;
		IMendixIdentifier identifier = getMendixObject().getValue(context, MemberNames.BackupConfigurationReferenceNodeData_MxObjectType_From.toString());
		if (identifier != null)
			result = mxmodelreflection.proxies.MxObjectType.load(context, identifier);
		return result;
	}

	/**
	 * Set value of BackupConfigurationReferenceNodeData_MxObjectType_From
	 * @param backupconfigurationreferencenodedata_mxobjecttype_from
	 */
	public final void setBackupConfigurationReferenceNodeData_MxObjectType_From(mxmodelreflection.proxies.MxObjectType backupconfigurationreferencenodedata_mxobjecttype_from)
	{
		setBackupConfigurationReferenceNodeData_MxObjectType_From(getContext(), backupconfigurationreferencenodedata_mxobjecttype_from);
	}

	/**
	 * Set value of BackupConfigurationReferenceNodeData_MxObjectType_From
	 * @param context
	 * @param backupconfigurationreferencenodedata_mxobjecttype_from
	 */
	public final void setBackupConfigurationReferenceNodeData_MxObjectType_From(IContext context, mxmodelreflection.proxies.MxObjectType backupconfigurationreferencenodedata_mxobjecttype_from)
	{
		if (backupconfigurationreferencenodedata_mxobjecttype_from == null)
			getMendixObject().setValue(context, MemberNames.BackupConfigurationReferenceNodeData_MxObjectType_From.toString(), null);
		else
			getMendixObject().setValue(context, MemberNames.BackupConfigurationReferenceNodeData_MxObjectType_From.toString(), backupconfigurationreferencenodedata_mxobjecttype_from.getMendixObject().getId());
	}

	/**
	 * @return value of BackupConfigurationReferenceNodeData_MxObjectType_To
	 */
	public final mxmodelreflection.proxies.MxObjectType getBackupConfigurationReferenceNodeData_MxObjectType_To() throws CoreException
	{
		return getBackupConfigurationReferenceNodeData_MxObjectType_To(getContext());
	}

	/**
	 * @param context
	 * @return value of BackupConfigurationReferenceNodeData_MxObjectType_To
	 */
	public final mxmodelreflection.proxies.MxObjectType getBackupConfigurationReferenceNodeData_MxObjectType_To(IContext context) throws CoreException
	{
		mxmodelreflection.proxies.MxObjectType result = null;
		IMendixIdentifier identifier = getMendixObject().getValue(context, MemberNames.BackupConfigurationReferenceNodeData_MxObjectType_To.toString());
		if (identifier != null)
			result = mxmodelreflection.proxies.MxObjectType.load(context, identifier);
		return result;
	}

	/**
	 * Set value of BackupConfigurationReferenceNodeData_MxObjectType_To
	 * @param backupconfigurationreferencenodedata_mxobjecttype_to
	 */
	public final void setBackupConfigurationReferenceNodeData_MxObjectType_To(mxmodelreflection.proxies.MxObjectType backupconfigurationreferencenodedata_mxobjecttype_to)
	{
		setBackupConfigurationReferenceNodeData_MxObjectType_To(getContext(), backupconfigurationreferencenodedata_mxobjecttype_to);
	}

	/**
	 * Set value of BackupConfigurationReferenceNodeData_MxObjectType_To
	 * @param context
	 * @param backupconfigurationreferencenodedata_mxobjecttype_to
	 */
	public final void setBackupConfigurationReferenceNodeData_MxObjectType_To(IContext context, mxmodelreflection.proxies.MxObjectType backupconfigurationreferencenodedata_mxobjecttype_to)
	{
		if (backupconfigurationreferencenodedata_mxobjecttype_to == null)
			getMendixObject().setValue(context, MemberNames.BackupConfigurationReferenceNodeData_MxObjectType_To.toString(), null);
		else
			getMendixObject().setValue(context, MemberNames.BackupConfigurationReferenceNodeData_MxObjectType_To.toString(), backupconfigurationreferencenodedata_mxobjecttype_to.getMendixObject().getId());
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == this)
			return true;

		if (obj != null && getClass().equals(obj.getClass()))
		{
			final objectbackuprestore.proxies.BackupConfigurationReferenceNodeData that = (objectbackuprestore.proxies.BackupConfigurationReferenceNodeData) obj;
			return getMendixObject().equals(that.getMendixObject());
		}
		return false;
	}

	@Override
	public int hashCode()
	{
		return getMendixObject().hashCode();
	}

	/**
	 * @return String name of this class
	 */
	public static String getType()
	{
		return "ObjectBackupRestore.BackupConfigurationReferenceNodeData";
	}

	/**
	 * @return String GUID from this object, format: ID_0000000000
	 * @deprecated Use getMendixObject().getId().toLong() to get a unique identifier for this object.
	 */
	@Override
	@Deprecated
	public String getGUID()
	{
		return "ID_" + getMendixObject().getId().toLong();
	}
}
