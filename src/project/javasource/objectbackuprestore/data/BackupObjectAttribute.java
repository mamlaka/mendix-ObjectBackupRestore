package objectbackuprestore.data;

import java.io.Serializable;

/**
 * @author marcel
 *
 */
public class BackupObjectAttribute implements Serializable {

	private static final long serialVersionUID = 8380804348628524714L;
	private String attributeName;
	private Serializable attributeValue;
	
	/**
	 * @return The member name
	 */
	public String getAttributeName() {
		return attributeName;
	}
	/**
	 * @param attributeName
	 */
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}
		
	/**
	 * @return The member value
	 */
	public Serializable getAttributeValue() {
		return attributeValue;
	}	
	/**
	 * @param attributeValue
	 */
	public void setAttributeValue(Serializable attributeValue) {
		this.attributeValue = attributeValue;
	}

	
}
