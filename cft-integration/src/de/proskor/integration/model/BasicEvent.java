package de.proskor.integration.model;

public interface BasicEvent {
	String getName();
	void setName(String name);
	EventType getType();
	void setType(EventType type);
}
