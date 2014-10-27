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
public class RestoreRequest
{
	private final IMendixObject restoreRequestMendixObject;

	private final IContext context;

	/**
	 * Internal name of this entity
	 */
	public static final String entityName = "ObjectBackupRestore.RestoreRequest";

	/**
	 * Enum describing members of this entity
	 */
	public enum MemberNames
	{
		ShowObjectDetails("ShowObjectDetails"),
		RestoreDateTime("RestoreDateTime"),
		FilterObjectName("FilterObjectName"),
		RestoreRequest_Backup("ObjectBackupRestore.RestoreRequest_Backup");

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

	public RestoreRequest(IContext context)
	{
		this(context, Core.instantiate(context, "ObjectBackupRestore.RestoreRequest"));
	}

	protected RestoreRequest(IContext context, IMendixObject restoreRequestMendixObject)
	{
		if (restoreRequestMendixObject == null)
			throw new IllegalArgumentException("The given object cannot be null.");
		if (!Core.isSubClassOf("ObjectBackupRestore.RestoreRequest", restoreRequestMendixObject.getType()))
			throw new IllegalArgumentException("The given object is not a ObjectBackupRestore.RestoreRequest");

		this.restoreRequestMendixObject = restoreRequestMendixObject;
		this.context = context;
	}

	/**
	 * @deprecated Use 'new RestoreRequest(Context)' instead. Note that the constructor will not insert the new object in the database.
	 */
	@Deprecated
	public static objectbackuprestore.proxies.RestoreRequest create(IContext context) throws CoreException
	{
		IMendixObject mendixObject = Core.create(context, "ObjectBackupRestore.RestoreRequest");
		return new objectbackuprestore.proxies.RestoreRequest(context, mendixObject);
	}

	/**
	 * @deprecated Use 'RestoreRequest.load(IContext, IMendixIdentifier)' instead.
	 */
	@Deprecated
	public static objectbackuprestore.proxies.RestoreRequest initialize(IContext context, IMendixIdentifier mendixIdentifier) throws CoreException
	{
		return objectbackuprestore.proxies.RestoreRequest.load(context, mendixIdentifier);
	}

	/**
	 * Initialize a proxy using context (recommended). This context will be used for security checking when the get- and set-methods without context parameters are called.
	 * The get- and set-methods with context parameter should be used when for instance sudo access is necessary (IContext.getSudoContext() can be used to obtain sudo access).
	 */
	public static objectbackuprestore.proxies.RestoreRequest initialize(IContext context, IMendixObject mendixObject)
	{
		return new objectbackuprestore.proxies.RestoreRequest(context, mendixObject);
	}

	public static objectbackuprestore.proxies.RestoreRequest load(IContext context, IMendixIdentifier mendixIdentifier) throws CoreException
	{
		IMendixObject mendixObject = Core.retrieveId(context, mendixIdentifier);
		return objectbackuprestore.proxies.RestoreRequest.initialize(context, mendixObject);
	}

	public static java.util.List<objectbackuprestore.proxies.RestoreRequest> load(IContext context, String xpathConstraint) throws CoreException
	{
		java.util.List<objectbackuprestore.proxies.RestoreRequest> result = new java.util.ArrayList<objectbackuprestore.proxies.RestoreRequest>();
		for (IMendixObject obj : Core.retrieveXPathQuery(context, "//ObjectBackupRestore.RestoreRequest" + xpathConstraint))
			result.add(objectbackuprestore.proxies.RestoreRequest.initialize(context, obj));
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
	 * @return value of ShowObjectDetails
	 */
	public final Boolean getShowObjectDetails()
	{
		return getShowObjectDetails(getContext());
	}

	/**
	 * @param context
	 * @return value of ShowObjectDetails
	 */
	public final Boolean getShowObjectDetails(IContext context)
	{
		return (Boolean) getMendixObject().getValue(context, MemberNames.ShowObjectDetails.toString());
	}

	/**
	 * Set value of ShowObjectDetails
	 * @param showobjectdetails
	 */
	public final void setShowObjectDetails(Boolean showobjectdetails)
	{
		setShowObjectDetails(getContext(), showobjectdetails);
	}

	/**
	 * Set value of ShowObjectDetails
	 * @param context
	 * @param showobjectdetails
	 */
	public final void setShowObjectDetails(IContext context, Boolean showobjectdetails)
	{
		getMendixObject().setValue(context, MemberNames.ShowObjectDetails.toString(), showobjectdetails);
	}

	/**
	 * @return value of RestoreDateTime
	 */
	public final java.util.Date getRestoreDateTime()
	{
		return getRestoreDateTime(getContext());
	}

	/**
	 * @param context
	 * @return value of RestoreDateTime
	 */
	public final java.util.Date getRestoreDateTime(IContext context)
	{
		return (java.util.Date) getMendixObject().getValue(context, MemberNames.RestoreDateTime.toString());
	}

	/**
	 * Set value of RestoreDateTime
	 * @param restoredatetime
	 */
	public final void setRestoreDateTime(java.util.Date restoredatetime)
	{
		setRestoreDateTime(getContext(), restoredatetime);
	}

	/**
	 * Set value of RestoreDateTime
	 * @param context
	 * @param restoredatetime
	 */
	public final void setRestoreDateTime(IContext context, java.util.Date restoredatetime)
	{
		getMendixObject().setValue(context, MemberNames.RestoreDateTime.toString(), restoredatetime);
	}

	/**
	 * @return value of FilterObjectName
	 */
	public final String getFilterObjectName()
	{
		return getFilterObjectName(getContext());
	}

	/**
	 * @param context
	 * @return value of FilterObjectName
	 */
	public final String getFilterObjectName(IContext context)
	{
		return (String) getMendixObject().getValue(context, MemberNames.FilterObjectName.toString());
	}

	/**
	 * Set value of FilterObjectName
	 * @param filterobjectname
	 */
	public final void setFilterObjectName(String filterobjectname)
	{
		setFilterObjectName(getContext(), filterobjectname);
	}

	/**
	 * Set value of FilterObjectName
	 * @param context
	 * @param filterobjectname
	 */
	public final void setFilterObjectName(IContext context, String filterobjectname)
	{
		getMendixObject().setValue(context, MemberNames.FilterObjectName.toString(), filterobjectname);
	}

	/**
	 * @return value of RestoreRequest_Backup
	 */
	public final objectbackuprestore.proxies.Backup getRestoreRequest_Backup() throws CoreException
	{
		return getRestoreRequest_Backup(getContext());
	}

	/**
	 * @param context
	 * @return value of RestoreRequest_Backup
	 */
	public final objectbackuprestore.proxies.Backup getRestoreRequest_Backup(IContext context) throws CoreException
	{
		objectbackuprestore.proxies.Backup result = null;
		IMendixIdentifier identifier = getMendixObject().getValue(context, MemberNames.RestoreRequest_Backup.toString());
		if (identifier != null)
			result = objectbackuprestore.proxies.Backup.load(context, identifier);
		return result;
	}

	/**
	 * Set value of RestoreRequest_Backup
	 * @param restorerequest_backup
	 */
	public final void setRestoreRequest_Backup(objectbackuprestore.proxies.Backup restorerequest_backup)
	{
		setRestoreRequest_Backup(getContext(), restorerequest_backup);
	}

	/**
	 * Set value of RestoreRequest_Backup
	 * @param context
	 * @param restorerequest_backup
	 */
	public final void setRestoreRequest_Backup(IContext context, objectbackuprestore.proxies.Backup restorerequest_backup)
	{
		if (restorerequest_backup == null)
			getMendixObject().setValue(context, MemberNames.RestoreRequest_Backup.toString(), null);
		else
			getMendixObject().setValue(context, MemberNames.RestoreRequest_Backup.toString(), restorerequest_backup.getMendixObject().getId());
	}

	/**
	 * @return the IMendixObject instance of this proxy for use in the Core interface.
	 */
	public final IMendixObject getMendixObject()
	{
		return restoreRequestMendixObject;
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
			final objectbackuprestore.proxies.RestoreRequest that = (objectbackuprestore.proxies.RestoreRequest) obj;
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
		return "ObjectBackupRestore.RestoreRequest";
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