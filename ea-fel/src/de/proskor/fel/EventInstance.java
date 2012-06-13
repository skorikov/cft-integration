package de.proskor.fel;

public interface EventInstance {
	public int getId();
	public String getGuid();
	public String getDescription();
	public String getAuthor();
	public EventType getEvent();
	public EventInstanceContainer getContainer();
}