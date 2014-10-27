package objectbackuprestore.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author marcel
 *
 */
public class BackupObject implements Serializable {
	
	private static final long serialVersionUID = -5414969151856340597L;
	
	private String completeObjectName;
	private long id;
	private BackupObjectAttribute keyAttribute; 
	private List<BackupObjectAttribute> attributeList = new ArrayList<>();
	private List<BackupObjectAssociation> referenceList = new ArrayList<>();	
	
	/**
	 * @return the name
	 */
	public String getCompleteObjectName() {
		return completeObjectName;
	}
	/**
	 * @param completeObjectName
	 */
	public void setCompleteObjectName(String completeObjectName) {
		this.completeObjectName = completeObjectName;
	}
	
	/**
	 * @return The id
	 */
	public long getId() {
		return id;
	}
	/**
	 * @param id
	 */
	public void setId(long id) {
		this.id = id;
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
	
	/**
	 * @return The list
	 */
	public List<BackupObjectAttribute> getAttributeList() {
		return attributeList;
	}
	
	/**
	 * @return the referenceList
	 */
	public List<BackupObjectAssociation> getReferenceList() {
		return referenceList;
	}
	

}
