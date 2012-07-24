package de.proskor.model;

public interface Diagram extends Entity {
	/** Diagram types. */
	String ACTIVITY = "Activity";
	String ANALYSIS = "Analysis";
	String COMPONENT = "Component";
	String CUSTOM = "Custom";
	String DEPLOYMENT = "Deployment";
	String LOGICAL = "Logical";
	String SEQUENCE = "Sequence";
	String STATECHART = "Statechart";
	String USECASE = "UseCase";

	/**
	 * Get type.
	 * @return type.
	 */
	String getType();

	/**
	 * Set type.
	 * @param type new type value.
	 */
	void setType(String type);

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
	 * Get diagram nodes.
	 * @return collection of diagram nodes.
	 */
	Collection<Node> getNodes();
}
