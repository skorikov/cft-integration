package de.proskor.model;

public interface Connector extends Entity {
	String getType();
	void setType(String type);

	Element getSource();
	void setSource(Element source);

	Element getTarget();
	void setTarget(Element target);
}
