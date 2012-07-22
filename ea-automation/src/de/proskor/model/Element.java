package de.proskor.model;

public interface Element extends Entity {
	/**
	 * Get element stereotype.
	 * @return stereotype.
	 */
	String getStereotype();

	/**
	 * Set element stereotype.
	 * @param stereotype new stereotype value.
	 */
	void setStereotype(String stereotype);

	/**
	 * Determines if the element is contained in another element.
	 * @return true if it is contained in another element, false otherwise.
	 */
	boolean isChild();

	/**
	 * Get the containing element.
	 * Throws IllegalStateException if isChild returns false.
	 * @return containing element.
	 */
	Element getParent();

	/**
	 * Get containing package.
	 * @return containing package.
	 */
	Package getPackage();

	/**
	 * Get contained elements.
	 * @return collection of contained elements.
	 */
	Collection<Element> getElements();

	/**
	 * Get associated tagged values.
	 * @return tagged values.
	 */
	Collection<TaggedValue> getTaggedValues();

	/**
	 * Get connectors.
	 * @return collection of connectors.
	 */
	Collection<Connector> getConnectors();

	/**
	 * Create a connector connecting this element to target.
	 * @param target target element.
	 * @return newly created connector.
	 */
	Connector connectTo(Element target);
}
