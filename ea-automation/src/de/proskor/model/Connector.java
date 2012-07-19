package de.proskor.model;

public interface Connector {
	int getId();
	String getGuid();

	String getName();
	void setName(String name);

	String getDescription();
	void setDescription(String description);

	String getStereotype();
	void setStereotype(String stereotype);

	String getType();
	void setType(String type);

	Element getSource();
	void setSource(Element source);

	Element getTarget();
	void setTarget(Element target);
}
