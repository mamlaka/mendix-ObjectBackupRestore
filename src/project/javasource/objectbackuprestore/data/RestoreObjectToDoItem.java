package objectbackuprestore.data;

import java.util.ArrayList;
import java.util.List;

import com.mendix.systemwideinterfaces.core.IMendixObject;

/**
 * @author marcel
 *
 */
public class RestoreObjectToDoItem {

	private IMendixObject object;
	private List<BackupObjectAssociation> associationList = new ArrayList<>();
	
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
	 * @return the associationList
	 */
	public List<BackupObjectAssociation> getAssociationList() {
		return associationList;
	}
	
}
