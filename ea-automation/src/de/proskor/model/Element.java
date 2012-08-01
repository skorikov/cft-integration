package de.proskor.model;

public interface Element extends Entity {
	/** Element types. */
	String ACTION = "Action";
	String ACTIVITY = "Activity";
	String ACTIVITYPARTITION = "ActivityPartition";
	String ACTIVITYREGION = "ActivityRegion";
	String ACTOR = "Actor";
	String ARTIFACT = "Artifact";
	String ASSOCIATION = "Association";
	String BOUNDARY = "Boundary";
	String CHANGE = "Change";
	String CLASS = "Class";
	String COLLABORATION = "Collaboration";
	String COMPONENT = "Component";
	String CONSTRAINT = "Constraint";
	String DECISION = "Decision";
	String DEPLOYMENTSPECIFICATION = "DeploymentSpecification";
	String DIAGRAMFRAME = "DiagramFrame";
	String EMBEDDEDELEMENT = "EmbeddedElement";
	String ENTRYPOINT = "EntryPoint";
	String EVENT = "Event";
	String EXCEPTIONHANDLER = "ExceptionHandler";
	String EXITPOINT = "ExitPoint";
	String EXPANSIONNODE = "ExpansionNode";
	String EXPANSIONREGION = "ExpansionRegion";
	String INTERACTIONFRAGMENT = "GUIElement InteractionFragment";
	String INTERACTIONOCCURENCE = "InteractionOccurrence";
	String INTERACTIONSTATE = "InteractionState";
	String INTERFACE = "Interface";
	String INTERRUPTIBLEACTIVITYREGION = "InterruptibleActivityRegion";
	String ISSUE = "Issue";
	String NODE = "Node";
	String NOTE = "Note";
	String OBJECT = "Object";
	String PACKAGE = "Package";
	String PARAMETER = "Parameter";
	String PART = "Part";
	String PORT = "Port";
	String PROVIDEDINTERFACE = "ProvidedInterface";
	String REPORT = "Report";
	String REQUIREDINTERFACE = "RequiredInterface";
	String REQUIREMENT = "Requirement";
	String SCREEN = "Screen";
	String SEQUENCE = "Sequence";
	String STATE = "State";
	String STATENODE = "StateNode";
	String SYNCHRONIZATION = "Synchronization";
	String TEXT = "Text";
	String TIMELINE = "TimeLine";
	String UMLDIAGRAM = "UMLDiagram";
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
	 * Get author.
	 * @return author.
	 */
	String getAuthor();

	/**
	 * Set author.
	 * @param author new author value.
	 */
	void setAuthor(String author);

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
	 * Set the parent.
	 * @param parent new parent element.
	 */
	void setParent(Element parent);

	/**
	 * Get containing package.
	 * @return containing package.
	 */
	Package getPackage();

	/**
	 * Set package.
	 * @param pkg new package.
	 */
	void setPackage(Package pkg);

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
