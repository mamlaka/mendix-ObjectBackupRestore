// This file was generated by Mendix Business Modeler.
//
// WARNING: Code you write here will be lost the next time you deploy the project.

package myfirstmodule.proxies;

import com.mendix.core.Core;
import com.mendix.core.CoreException;
import com.mendix.systemwideinterfaces.core.IContext;
import com.mendix.systemwideinterfaces.core.IMendixIdentifier;
import com.mendix.systemwideinterfaces.core.IMendixObject;

/**
 * 
 */
public class TransientIncidentPivotTableParameters
{
	private final IMendixObject transientIncidentPivotTableParametersMendixObject;

	private final IContext context;

	/**
	 * Internal name of this entity
	 */
	public static final String entityName = "MyFirstModule.TransientIncidentPivotTableParameters";

	/**
	 * Enum describing members of this entity
	 */
	public enum MemberNames
	{
		fromDate("fromDate"),
		toDate("toDate"),
		TransientIncidentPivotTableParameters_Employee("MyFirstModule.TransientIncidentPivotTableParameters_Employee");

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

	public TransientIncidentPivotTableParameters(IContext context)
	{
		this(context, Core.instantiate(context, "MyFirstModule.TransientIncidentPivotTableParameters"));
	}

	protected TransientIncidentPivotTableParameters(IContext context, IMendixObject transientIncidentPivotTableParametersMendixObject)
	{
		if (transientIncidentPivotTableParametersMendixObject == null)
			throw new IllegalArgumentException("The given object cannot be null.");
		if (!Core.isSubClassOf("MyFirstModule.TransientIncidentPivotTableParameters", transientIncidentPivotTableParametersMendixObject.getType()))
			throw new IllegalArgumentException("The given object is not a MyFirstModule.TransientIncidentPivotTableParameters");

		this.transientIncidentPivotTableParametersMendixObject = transientIncidentPivotTableParametersMendixObject;
		this.context = context;
	}

	/**
	 * @deprecated Use 'new TransientIncidentPivotTableParameters(Context)' instead. Note that the constructor will not insert the new object in the database.
	 */
	@Deprecated
	public static myfirstmodule.proxies.TransientIncidentPivotTableParameters create(IContext context) throws CoreException
	{
		IMendixObject mendixObject = Core.create(context, "MyFirstModule.TransientIncidentPivotTableParameters");
		return new myfirstmodule.proxies.TransientIncidentPivotTableParameters(context, mendixObject);
	}

	/**
	 * @deprecated Use 'TransientIncidentPivotTableParameters.load(IContext, IMendixIdentifier)' instead.
	 */
	@Deprecated
	public static myfirstmodule.proxies.TransientIncidentPivotTableParameters initialize(IContext context, IMendixIdentifier mendixIdentifier) throws CoreException
	{
		return myfirstmodule.proxies.TransientIncidentPivotTableParameters.load(context, mendixIdentifier);
	}

	/**
	 * Initialize a proxy using context (recommended). This context will be used for security checking when the get- and set-methods without context parameters are called.
	 * The get- and set-methods with context parameter should be used when for instance sudo access is necessary (IContext.getSudoContext() can be used to obtain sudo access).
	 */
	public static myfirstmodule.proxies.TransientIncidentPivotTableParameters initialize(IContext context, IMendixObject mendixObject)
	{
		return new myfirstmodule.proxies.TransientIncidentPivotTableParameters(context, mendixObject);
	}

	public static myfirstmodule.proxies.TransientIncidentPivotTableParameters load(IContext context, IMendixIdentifier mendixIdentifier) throws CoreException
	{
		IMendixObject mendixObject = Core.retrieveId(context, mendixIdentifier);
		return myfirstmodule.proxies.TransientIncidentPivotTableParameters.initialize(context, mendixObject);
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
	 * @return value of fromDate
	 */
	public final java.util.Date getfromDate()
	{
		return getfromDate(getContext());
	}

	/**
	 * @param context
	 * @return value of fromDate
	 */
	public final java.util.Date getfromDate(IContext context)
	{
		return (java.util.Date) getMendixObject().getValue(context, MemberNames.fromDate.toString());
	}

	/**
	 * Set value of fromDate
	 * @param fromdate
	 */
	public final void setfromDate(java.util.Date fromdate)
	{
		setfromDate(getContext(), fromdate);
	}

	/**
	 * Set value of fromDate
	 * @param context
	 * @param fromdate
	 */
	public final void setfromDate(IContext context, java.util.Date fromdate)
	{
		getMendixObject().setValue(context, MemberNames.fromDate.toString(), fromdate);
	}

	/**
	 * @return value of toDate
	 */
	public final java.util.Date gettoDate()
	{
		return gettoDate(getContext());
	}

	/**
	 * @param context
	 * @return value of toDate
	 */
	public final java.util.Date gettoDate(IContext context)
	{
		return (java.util.Date) getMendixObject().getValue(context, MemberNames.toDate.toString());
	}

	/**
	 * Set value of toDate
	 * @param todate
	 */
	public final void settoDate(java.util.Date todate)
	{
		settoDate(getContext(), todate);
	}

	/**
	 * Set value of toDate
	 * @param context
	 * @param todate
	 */
	public final void settoDate(IContext context, java.util.Date todate)
	{
		getMendixObject().setValue(context, MemberNames.toDate.toString(), todate);
	}

	/**
	 * @return value of TransientIncidentPivotTableParameters_Employee
	 */
	public final myfirstmodule.proxies.Employee getTransientIncidentPivotTableParameters_Employee() throws CoreException
	{
		return getTransientIncidentPivotTableParameters_Employee(getContext());
	}

	/**
	 * @param context
	 * @return value of TransientIncidentPivotTableParameters_Employee
	 */
	public final myfirstmodule.proxies.Employee getTransientIncidentPivotTableParameters_Employee(IContext context) throws CoreException
	{
		myfirstmodule.proxies.Employee result = null;
		IMendixIdentifier identifier = getMendixObject().getValue(context, MemberNames.TransientIncidentPivotTableParameters_Employee.toString());
		if (identifier != null)
			result = myfirstmodule.proxies.Employee.load(context, identifier);
		return result;
	}

	/**
	 * Set value of TransientIncidentPivotTableParameters_Employee
	 * @param transientincidentpivottableparameters_employee
	 */
	public final void setTransientIncidentPivotTableParameters_Employee(myfirstmodule.proxies.Employee transientincidentpivottableparameters_employee)
	{
		setTransientIncidentPivotTableParameters_Employee(getContext(), transientincidentpivottableparameters_employee);
	}

	/**
	 * Set value of TransientIncidentPivotTableParameters_Employee
	 * @param context
	 * @param transientincidentpivottableparameters_employee
	 */
	public final void setTransientIncidentPivotTableParameters_Employee(IContext context, myfirstmodule.proxies.Employee transientincidentpivottableparameters_employee)
	{
		if (transientincidentpivottableparameters_employee == null)
			getMendixObject().setValue(context, MemberNames.TransientIncidentPivotTableParameters_Employee.toString(), null);
		else
			getMendixObject().setValue(context, MemberNames.TransientIncidentPivotTableParameters_Employee.toString(), transientincidentpivottableparameters_employee.getMendixObject().getId());
	}

	/**
	 * @return the IMendixObject instance of this proxy for use in the Core interface.
	 */
	public final IMendixObject getMendixObject()
	{
		return transientIncidentPivotTableParametersMendixObject;
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
			final myfirstmodule.proxies.TransientIncidentPivotTableParameters that = (myfirstmodule.proxies.TransientIncidentPivotTableParameters) obj;
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
		return "MyFirstModule.TransientIncidentPivotTableParameters";
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
