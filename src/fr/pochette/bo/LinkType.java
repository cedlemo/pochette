package fr.pochette.bo;

public class LinkType {

	private int idType;
	private String label;
	/**
	 * @return the idType
	 */
	public int getIdType() {
		return idType;
	}
	/**
	 * @param idType the idType to set
	 */
	public void setIdType(int idType) {
		this.idType = idType;
	}
	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}
	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}
	/**
	 * @param idType
	 * @param label
	 */
	public LinkType(int idType, String label) {
		super();
		this.setIdType( idType);
		this.setLabel(label);
	}
	
	
}
