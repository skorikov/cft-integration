package de.proskor.model;

public interface Diagram extends Entity {
	/** Diagram types. */
	String ACTIVITY = "Activity";
	String ANALYSIS = "Analysis";
	String COMPONENT = "Component";
	String CUSTOM = "Custom";
	String DEPLOYMENT = "Deployment";
	String LOGICAL = "Logical";
	String OBJECT = "Object";
	String SEQUENCE = "Sequence";
	String STATECHART = "Statechart";
	String USECASE = "UseCase";

	/**
	 * Get type.
	 * @return type.
	 */
	String getType();

	/**
	 * Get stereotype.
	 * @return stereotype.
	 */
	String getStereotype();

	/**
	 * Set stereotype.
	 * @param stereotype new stereotype value.
	 */
	void setStereotype(String stereotype);

	/**
	 * Get containing package.
	 * @return containing package.
	 */
	Package getPackage();

	/**
	 * Set parent package.
	 * @param pkg parent package.
	 */
	void setPackage(Package pkg);

	/**
	 * Get diagram nodes.
	 * @return collection of diagram nodes.
	 */
	Collection<Node> getNodes();

	/**
	 * Create node for an element.
	 * @param element element.
	 * @return newly created node.
	 */
	Node createNode(Element element);
}
