package objectbackuprestore.data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author marcel
 * Many ObjectInputStream examples show a loop where the readObject method would return null when no more objects can be read.
 * This is only true if the data ends with a null, you will get an EOFException when calling readObject when reading beyond 
 * the last object in the data
 * To prevent the exception and to provide an additional integrity check, an object of this class is written to the backup last 
 */
public class BackupDataTerminator implements Serializable {
	

	private static final long serialVersionUID = 6108218884570084202L;

	private Date creationDateTime;
	private int objectCount;
	private int referencedObjectCount;
	private int fileDocumentCount;
	private String groupCode;
	private String description;
	
	/**
	 * Default constructor
	 */
	public BackupDataTerminator() {
	}

	/**
	 * @return the creationDateTime
	 */
	public Date getCreationDateTime() {
		return creationDateTime;
	}

	/**
	 * @param creationDateTime the creationDateTime to set
	 */
	public void setCreationDateTime(Date creationDateTime) {
		this.creationDateTime = creationDateTime;
	}

	/**
	 * @return the objectCount
	 */
	public int getObjectCount() {
		return objectCount;
	}

	/**
	 * @param objectCount the objectCount to set
	 */
	public void setObjectCount(int objectCount) {
		this.objectCount = objectCount;
	}

	/**
	 * @return the referencedObjectCount
	 */
	public int getReferencedObjectCount() {
		return referencedObjectCount;
	}

	/**
	 * @param referencedObjectCount the referencedObjectCount to set
	 */
	public void setReferencedObjectCount(int referencedObjectCount) {
		this.referencedObjectCount = referencedObjectCount;
	}

	/**
	 * @return the fileDocumentCount
	 */
	public int getFileDocumentCount() {
		return fileDocumentCount;
	}

	/**
	 * @param fileDocumentCount the fileDocumentCount to set
	 */
	public void setFileDocumentCount(int fileDocumentCount) {
		this.fileDocumentCount = fileDocumentCount;
	}

	/**
	 * @return the groupCode
	 */
	public String getGroupCode() {
		return groupCode;
	}

	/**
	 * @param groupCode the groupCode to set
	 */
	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
}
