package objectbackuprestore.data;

import java.io.Serializable;

/**
 * @author marcel
 *
 * Referenced object identification, used for associations
 */
public class ReferencedObjectId implements Serializable {

	private static final long serialVersionUID = 2833072561957710630L;
	private long objectId;
	private BackupObjectAttribute keyAttribute;
	
	/**
	 * @return the objectId
	 */
	public long getObjectId() {
		return objectId;
	}
	
	/**
	 * @param objectId the objectId to set
	 */
	public void setObjectId(long objectId) {
		this.objectId = objectId;
	}
	
	/**
	 * @return the keyAttribute
	 */
	public BackupObjectAttribute getKeyAttribute() {
		return keyAttribute;
	}
	
	/**
	 * @param keyAttribute the keyAttribute to set
	 */
	public void setKeyAttribute(BackupObjectAttribute keyAttribute) {
		this.keyAttribute = keyAttribute;
	}
	
}
