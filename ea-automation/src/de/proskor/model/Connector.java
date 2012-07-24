package de.proskor.model;

public interface Connector extends Entity {
	/** Connector types. */
	String AGGREGATION = "Aggregation";
	String ASSEMBLY = "Assembly";
	String ASSOCIATION = "Association";
	String COLLABORATION = "Collaboration";
	String COMMUNICATIONPATH = "CommunicationPath";
	String CONNECTOR = "Connector";
	String CONTROLFLOW = "ControlFlow";
	String DELEGATE = "Delegate";
	String DEPENDENCY = "Dependency";
	String DEPLOYMENT = "Deployment";
	String ERLINK = "ERLink";
	String GENERALIZATION = "Generalization";
	String INFORMATIONFLOW = "InformationFlow";
	String INSTANTIATION = "Instantiation";
	String INTERRUPTFLOW = "InterruptFlow";
	String MANIFEST = "Manifest";
	String NESTING = "Nesting";
	String NOTELINK = "NoteLink";
	String OBJECTFLOW = "ObjectFlow";
	String PACKAGE = "Package";
	String REALIZATION = "Realization";
	String SEQUENCE = "Sequence";
	String STATEFLOW = "StateFlow";
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
	 * Get connector source.
	 * @return connector source.
	 */
	Element getSource();
	
	/**
	 * Set connector source.
	 * @param source new connector source.
	 */
	void setSource(Element source);

	/**
	 * Get connector target.
	 * @return connector target.
	 */
	Element getTarget();

	/**
	 * Set connector target.
	 * @param target new connector target.
	 */
	void setTarget(Element target);
}
