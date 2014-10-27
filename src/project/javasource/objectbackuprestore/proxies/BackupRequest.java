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
public class BackupRequest
{
	private final IMendixObject backupRequestMendixObject;

	private final IContext context;

	/**
	 * Internal name of this entity
	 */
	public static final String entityName = "ObjectBackupRestore.BackupRequest";

	/**
	 * Enum describing members of this entity
	 */
	public enum MemberNames
	{
		Name("Name"),
		XPathConstraint("XPathConstraint"),
		RunBackup_BackupConfiguration("ObjectBackupRestore.RunBackup_BackupConfiguration");

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

	public BackupRequest(IContext context)
	{
		this(context, Core.instantiate(context, "ObjectBackupRestore.BackupRequest"));
	}

	protected BackupRequest(IContext context, IMendixObject backupRequestMendixObject)
	{
		if (backupRequestMendixObject == null)
			throw new IllegalArgumentException("The given object cannot be null.");
		if (!Core.isSubClassOf("ObjectBackupRestore.BackupRequest", backupRequestMendixObject.getType()))
			throw new IllegalArgumentException("The given object is not a ObjectBackupRestore.BackupRequest");

		this.backupRequestMendixObject = backupRequestMendixObject;
		this.context = context;
	}

	/**
	 * @deprecated Use 'new BackupRequest(Context)' instead. Note that the constructor will not insert the new object in the database.
	 */
	@Deprecated
	public static objectbackuprestore.proxies.BackupRequest create(IContext context) throws CoreException
	{
		IMendixObject mendixObject = Core.create(context, "ObjectBackupRestore.BackupRequest");
		return new objectbackuprestore.proxies.BackupRequest(context, mendixObject);
	}

	/**
	 * @deprecated Use 'BackupRequest.load(IContext, IMendixIdentifier)' instead.
	 */
	@Deprecated
	public static objectbackuprestore.proxies.BackupRequest initialize(IContext context, IMendixIdentifier mendixIdentifier) throws CoreException
	{
		return objectbackuprestore.proxies.BackupRequest.load(context, mendixIdentifier);
	}

	/**
	 * Initialize a proxy using context (recommended). This context will be used for security checking when the get- and set-methods without context parameters are called.
	 * The get- and set-methods with context parameter should be used when for instance sudo access is necessary (IContext.getSudoContext() can be used to obtain sudo access).
	 */
	public static objectbackuprestore.proxies.BackupRequest initialize(IContext context, IMendixObject mendixObject)
	{
		return new objectbackuprestore.proxies.BackupRequest(context, mendixObject);
	}

	public static objectbackuprestore.proxies.BackupRequest load(IContext context, IMendixIdentifier mendixIdentifier) throws CoreException
	{
		IMendixObject mendixObject = Core.retrieveId(context, mendixIdentifier);
		return objectbackuprestore.proxies.BackupRequest.initialize(context, mendixObject);
	}

	public static java.util.List<objectbackuprestore.proxies.BackupRequest> load(IContext context, String xpathConstraint) throws CoreException
	{
		java.util.List<objectbackuprestore.proxies.BackupRequest> result = new java.util.ArrayList<objectbackuprestore.proxies.BackupRequest>();
		for (IMendixObject obj : Core.retrieveXPathQuery(context, "//ObjectBackupRestore.BackupRequest" + xpathConstraint))
			result.add(objectbackuprestore.proxies.BackupRequest.initialize(context, obj));
		return result;
	}

	/**
	 * Commit the changes made on this proxy object.
	 */
	public final void commit() throws CoreException
	{
		Core.commit(context, getMendixObject());
	}

	/**
	 * Commit the changes made on this proxy object using the specified context.
	 */
	public final void commit(IContext context) throws CoreException
	{
		Core.commit(context, getMendixObject());
	}

	/**
	 * Delete the object.
	 */
	public final void delete()
	{
		Core.delete(context, getMendixObject());
	}

	/**
	 * Delete the object using the specified context.
	 */
	public final void delete(IContext context)
	{
		Core.delete(context, getMendixObject());
	}
	/**
	 * @return value of Name
	 */
	public final String getName()
	{
		return getName(getContext());
	}

	/**
	 * @param context
	 * @return value of Name
	 */
	public final String getName(IContext context)
	{
		return (String) getMendixObject().getValue(context, MemberNames.Name.toString());
	}

	/**
	 * Set value of Name
	 * @param name
	 */
	public final void setName(String name)
	{
		setName(getContext(), name);
	}

	/**
	 * Set value of Name
	 * @param context
	 * @param name
	 */
	public final void setName(IContext context, String name)
	{
		getMendixObject().setValue(context, MemberNames.Name.toString(), name);
	}

	/**
	 * @return value of XPathConstraint
	 */
	public final String getXPathConstraint()
	{
		return getXPathConstraint(getContext());
	}

	/**
	 * @param context
	 * @return value of XPathConstraint
	 */
	public final String getXPathConstraint(IContext context)
	{
		return (String) getMendixObject().getValue(context, MemberNames.XPathConstraint.toString());
	}

	/**
	 * Set value of XPathConstraint
	 * @param xpathconstraint
	 */
	public final void setXPathConstraint(String xpathconstraint)
	{
		setXPathConstraint(getContext(), xpathconstraint);
	}

	/**
	 * Set value of XPathConstraint
	 * @param context
	 * @param xpathconstraint
	 */
	public final void setXPathConstraint(IContext context, String xpathconstraint)
	{
		getMendixObject().setValue(context, MemberNames.XPathConstraint.toString(), xpathconstraint);
	}

	/**
	 * @return value of RunBackup_BackupConfiguration
	 */
	public final objectbackuprestore.proxies.BackupConfiguration getRunBackup_BackupConfiguration() throws CoreException
	{
		return getRunBackup_BackupConfiguration(getContext());
	}

	/**
	 * @param context
	 * @return value of RunBackup_BackupConfiguration
	 */
	public final objectbackuprestore.proxies.BackupConfiguration getRunBackup_BackupConfiguration(IContext context) throws CoreException
	{
		objectbackuprestore.proxies.BackupConfiguration result = null;
		IMendixIdentifier identifier = getMendixObject().getValue(context, MemberNames.RunBackup_BackupConfiguration.toString());
		if (identifier != null)
			result = objectbackuprestore.proxies.BackupConfiguration.load(context, identifier);
		return result;
	}

	/**
	 * Set value of RunBackup_BackupConfiguration
	 * @param runbackup_backupconfiguration
	 */
	public final void setRunBackup_BackupConfiguration(objectbackuprestore.proxies.BackupConfiguration runbackup_backupconfiguration)
	{
		setRunBackup_BackupConfiguration(getContext(), runbackup_backupconfiguration);
	}

	/**
	 * Set value of RunBackup_BackupConfiguration
	 * @param context
	 * @param runbackup_backupconfiguration
	 */
	public final void setRunBackup_BackupConfiguration(IContext context, objectbackuprestore.proxies.BackupConfiguration runbackup_backupconfiguration)
	{
		if (runbackup_backupconfiguration == null)
			getMendixObject().setValue(context, MemberNames.RunBackup_BackupConfiguration.toString(), null);
		else
			getMendixObject().setValue(context, MemberNames.RunBackup_BackupConfiguration.toString(), runbackup_backupconfiguration.getMendixObject().getId());
	}

	/**
	 * @return the IMendixObject instance of this proxy for use in the Core interface.
	 */
	public final IMendixObject getMendixObject()
	{
		return backupRequestMendixObject;
	}

	/**
	 * @return the IContext instance of this proxy, or null if no IContext instance was specified at initialization.
	 */
	public final IContext getContext()
	{
		return context;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == this)
			return true;

		if (obj != null && getClass().equals(obj.getClass()))
		{
			final objectbackuprestore.proxies.BackupRequest that = (objectbackuprestore.proxies.BackupRequest) obj;
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
		return "ObjectBackupRestore.BackupRequest";
	}

	/**
	 * @return String GUID from this object, format: ID_0000000000
	 * @deprecated Use getMendixObject().getId().toLong() to get a unique identifier for this object.
	 */
	@Deprecated
	public String getGUID()
	{
		return "ID_" + getMendixObject().getId().toLong();
	}
}