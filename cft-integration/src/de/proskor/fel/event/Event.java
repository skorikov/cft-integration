package de.proskor.fel.event;

import de.proskor.fel.container.EventContainer;

public interface Event {
	public String getDescription();
	public String getAuthor();
	public EventContainer getContainer();
}
