package objectbackuprestore.data;

import java.io.Serializable;

/**
 * @author marcel
 * File document contents
 */
public class BackupObjectFileDocumentContents implements Serializable {

	
	private static final long serialVersionUID = -2090232396396114331L;

	private boolean hasContents;
	
	private byte[] contents;

	/**
	 * @return the hasContents
	 */
	public boolean isHasContents() {
		return hasContents;
	}

	/**
	 * @param hasContents the hasContents to set
	 */
	public void setHasContents(boolean hasContents) {
		this.hasContents = hasContents;
	}

	/**
	 * @return the contents
	 */
	public byte[] getContents() {
		return contents;
	}

	/**
	 * @param contents the contents to set
	 */
	public void setContents(byte[] contents) {
		this.contents = contents;
	}
	
	
}
