package de.proskor.fel.model.event;

import de.proskor.fel.model.container.EventContainer;

public interface Event {
	public String getDescription();
	public String getAuthor();
	public EventContainer getContainer();
}
