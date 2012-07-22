package de.proskor.model;

public interface Connector extends Entity {
	/**
	 * Get connector stereotype.
	 * @return stereotype.
	 */
	String getStereotype();

	/**
	 * Set connector stereotype.
	 * @param stereotype new stereotype value.
	 */
	void setStereotype(String stereotype);

	String getType();
	void setType(String type);

	Element getSource();
	void setSource(Element source);

	Element getTarget();
	void setTarget(Element target);
}
