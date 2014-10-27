package objectbackuprestore.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author marcel
 *
 */
public class BackupObjectAssociation implements Serializable {
	
	private static final long serialVersionUID = 7141183101293531981L;

	private String associationName;
	private String referencedEntityName;
	private boolean isReferenceSet;
	private List<ReferencedObjectId> referenceIdList= new ArrayList<>();
	
	/**
	 * @return the associationName
	 */
	public String getAssociationName() {
		return associationName;
	}
	/**
	 * @param associationName the associationName to set
	 */
	public void setAssociationName(String associationName) {
		this.associationName = associationName;
	}
	
	/**
	 * @return the referencedEntityName
	 */
	public String getReferencedEntityName() {
		return referencedEntityName;
	}
	/**
	 * @param referencedEntityName the referencedEntityName to set
	 */
	public void setReferencedEntityName(String referencedEntityName) {
		this.referencedEntityName = referencedEntityName;
	}
		
	/**
	 * @return the isReferenceSet
	 */
	public boolean isReferenceSet() {
		return isReferenceSet;
	}
	
	/**
	 * @param isReferenceSet the isReferenceSet to set
	 */
	public void setReferenceSet(boolean isReferenceSet) {
		this.isReferenceSet = isReferenceSet;
	}
	
	/**
	 * @return the referenceIdList
	 */
	public List<ReferencedObjectId> getReferenceIdList() {
		return referenceIdList;
	}
	
}
