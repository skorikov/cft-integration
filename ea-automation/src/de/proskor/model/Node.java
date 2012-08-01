package de.proskor.model;

/**
 * A diagram node.
 */
public interface Node extends Identity, Rectangle {
	/** Note types. */
	String NODE = "";

	/**
	 * Get node z-layer.
	 * @return z-layer.
	 */
	int getSequence();

	/**
	 * Set node z-layer.
	 * @param sequence new z-layer value.
	 */
	void setSequence(int sequence);

	/**
	 * Get containing diagram.
	 * @return containing diagram.
	 */
	Diagram getDiagram();

	/**
	 * Get associated element.
	 * @return associated element.
	 */
	Element getElement();

	/**
	 * Set associated element.
	 * @param element element.
	 */
	void setElement(Element element);
}
