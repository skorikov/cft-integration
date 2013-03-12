package de.proskor.integration.model;

public interface BasicEvent extends Source, Node {
	String getName();
	void setName(String name);

	EventType getType();
	void setType(EventType type);
}
