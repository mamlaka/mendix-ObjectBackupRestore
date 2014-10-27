package objectbackuprestore.data;

import com.mendix.systemwideinterfaces.core.IMendixObject;

/**
 * @author marcel
 *
 */
public class MissingReferenceItem {

	private IMendixObject object;
	private BackupObjectAssociation association;
	
	/**
	 * @return the object
	 */
	public IMendixObject getObject() {
		return object;
	}
	/**
	 * @param object the object to set
	 */
	public void setObject(IMendixObject object) {
		this.object = object;
	}
	/**
	 * @return the association
	 */
	public BackupObjectAssociation getAssociation() {
		return association;
	}
	/**
	 * @param association the association to set
	 */
	public void setAssociation(BackupObjectAssociation association) {
		this.association = association;
	}

	
}
