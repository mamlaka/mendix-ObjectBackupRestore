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
public class RestoreLog
{
	private final IMendixObject restoreLogMendixObject;

	private final IContext context;

	/**
	 * Internal name of this entity
	 */
	public static final String entityName = "ObjectBackupRestore.RestoreLog";

	/**
	 * Enum describing members of this entity
	 */
	public enum MemberNames
	{
		LogDateTime("LogDateTime"),
		Message("Message"),
		RestoreLog_RestoreRequest("ObjectBackupRestore.RestoreLog_RestoreRequest");

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

	public RestoreLog(IContext context)
	{
		this(context, Core.instantiate(context, "ObjectBackupRestore.RestoreLog"));
	}

	protected RestoreLog(IContext context, IMendixObject restoreLogMendixObject)
	{
		if (restoreLogMendixObject == null)
			throw new IllegalArgumentException("The given object cannot be null.");
		if (!Core.isSubClassOf("ObjectBackupRestore.RestoreLog", restoreLogMendixObject.getType()))
			throw new IllegalArgumentException("The given object is not a ObjectBackupRestore.RestoreLog");

		this.restoreLogMendixObject = restoreLogMendixObject;
		this.context = context;
	}

	/**
	 * @deprecated Use 'new RestoreLog(Context)' instead. Note that the constructor will not insert the new object in the database.
	 */
	@Deprecated
	public static objectbackuprestore.proxies.RestoreLog create(IContext context) throws CoreException
	{
		IMendixObject mendixObject = Core.create(context, "ObjectBackupRestore.RestoreLog");
		return new objectbackuprestore.proxies.RestoreLog(context, mendixObject);
	}

	/**
	 * @deprecated Use 'RestoreLog.load(IContext, IMendixIdentifier)' instead.
	 */
	@Deprecated
	public static objectbackuprestore.proxies.RestoreLog initialize(IContext context, IMendixIdentifier mendixIdentifier) throws CoreException
	{
		return objectbackuprestore.proxies.RestoreLog.load(context, mendixIdentifier);
	}

	/**
	 * Initialize a proxy using context (recommended). This context will be used for security checking when the get- and set-methods without context parameters are called.
	 * The get- and set-methods with context parameter should be used when for instance sudo access is necessary (IContext.getSudoContext() can be used to obtain sudo access).
	 */
	public static objectbackuprestore.proxies.RestoreLog initialize(IContext context, IMendixObject mendixObject)
	{
		return new objectbackuprestore.proxies.RestoreLog(context, mendixObject);
	}

	public static objectbackuprestore.proxies.RestoreLog load(IContext context, IMendixIdentifier mendixIdentifier) throws CoreException
	{
		IMendixObject mendixObject = Core.retrieveId(context, mendixIdentifier);
		return objectbackuprestore.proxies.RestoreLog.initialize(context, mendixObject);
	}

	public static java.util.List<objectbackuprestore.proxies.RestoreLog> load(IContext context, String xpathConstraint) throws CoreException
	{
		java.util.List<objectbackuprestore.proxies.RestoreLog> result = new java.util.ArrayList<objectbackuprestore.proxies.RestoreLog>();
		for (IMendixObject obj : Core.retrieveXPathQuery(context, "//ObjectBackupRestore.RestoreLog" + xpathConstraint))
			result.add(objectbackuprestore.proxies.RestoreLog.initialize(context, obj));
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
	 * @return value of LogDateTime
	 */
	public final java.util.Date getLogDateTime()
	{
		return getLogDateTime(getContext());
	}

	/**
	 * @param context
	 * @return value of LogDateTime
	 */
	public final java.util.Date getLogDateTime(IContext context)
	{
		return (java.util.Date) getMendixObject().getValue(context, MemberNames.LogDateTime.toString());
	}

	/**
	 * Set value of LogDateTime
	 * @param logdatetime
	 */
	public final void setLogDateTime(java.util.Date logdatetime)
	{
		setLogDateTime(getContext(), logdatetime);
	}

	/**
	 * Set value of LogDateTime
	 * @param context
	 * @param logdatetime
	 */
	public final void setLogDateTime(IContext context, java.util.Date logdatetime)
	{
		getMendixObject().setValue(context, MemberNames.LogDateTime.toString(), logdatetime);
	}

	/**
	 * @return value of Message
	 */
	public final String getMessage()
	{
		return getMessage(getContext());
	}

	/**
	 * @param context
	 * @return value of Message
	 */
	public final String getMessage(IContext context)
	{
		return (String) getMendixObject().getValue(context, MemberNames.Message.toString());
	}

	/**
	 * Set value of Message
	 * @param message
	 */
	public final void setMessage(String message)
	{
		setMessage(getContext(), message);
	}

	/**
	 * Set value of Message
	 * @param context
	 * @param message
	 */
	public final void setMessage(IContext context, String message)
	{
		getMendixObject().setValue(context, MemberNames.Message.toString(), message);
	}

	/**
	 * @return value of RestoreLog_RestoreRequest
	 */
	public final objectbackuprestore.proxies.RestoreRequest getRestoreLog_RestoreRequest() throws CoreException
	{
		return getRestoreLog_RestoreRequest(getContext());
	}

	/**
	 * @param context
	 * @return value of RestoreLog_RestoreRequest
	 */
	public final objectbackuprestore.proxies.RestoreRequest getRestoreLog_RestoreRequest(IContext context) throws CoreException
	{
		objectbackuprestore.proxies.RestoreRequest result = null;
		IMendixIdentifier identifier = getMendixObject().getValue(context, MemberNames.RestoreLog_RestoreRequest.toString());
		if (identifier != null)
			result = objectbackuprestore.proxies.RestoreRequest.load(context, identifier);
		return result;
	}

	/**
	 * Set value of RestoreLog_RestoreRequest
	 * @param restorelog_restorerequest
	 */
	public final void setRestoreLog_RestoreRequest(objectbackuprestore.proxies.RestoreRequest restorelog_restorerequest)
	{
		setRestoreLog_RestoreRequest(getContext(), restorelog_restorerequest);
	}

	/**
	 * Set value of RestoreLog_RestoreRequest
	 * @param context
	 * @param restorelog_restorerequest
	 */
	public final void setRestoreLog_RestoreRequest(IContext context, objectbackuprestore.proxies.RestoreRequest restorelog_restorerequest)
	{
		if (restorelog_restorerequest == null)
			getMendixObject().setValue(context, MemberNames.RestoreLog_RestoreRequest.toString(), null);
		else
			getMendixObject().setValue(context, MemberNames.RestoreLog_RestoreRequest.toString(), restorelog_restorerequest.getMendixObject().getId());
	}

	/**
	 * @return the IMendixObject instance of this proxy for use in the Core interface.
	 */
	public final IMendixObject getMendixObject()
	{
		return restoreLogMendixObject;
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
			final objectbackuprestore.proxies.RestoreLog that = (objectbackuprestore.proxies.RestoreLog) obj;
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
		return "ObjectBackupRestore.RestoreLog";
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
