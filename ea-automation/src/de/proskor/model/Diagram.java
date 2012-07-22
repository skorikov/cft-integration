package de.proskor.model;

public interface Diagram extends Entity {
	/**
	 * Get diagram stereotype.
	 * @return stereotype.
	 */
	String getStereotype();

	/**
	 * Set diagram stereotype.
	 * @param stereotype new stereotype value.
	 */
	void setStereotype(String stereotype);

	Package getPackage();
	Collection<Node> getNodes();
}
