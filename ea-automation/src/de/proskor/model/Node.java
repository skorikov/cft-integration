package de.proskor.model;

/**
 * A diagram node.
 */
public interface Node extends Identity, Rectangle {
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
}
