package de.proskor.fel.container;

import java.util.List;

import de.proskor.fel.event.Event;

public interface EventContainer {
	public List<? extends Event> getEvents();
	public String getName();

	/**
	 * Soll weggehen. Bei EventType in createEventType o.ä. umbenennen.
	 * @param name
	 * @return
	 */
	@Deprecated
	public Event createEvent(String name);
	
	public EventContainer getParent();
	public List<? extends EventContainer> getChildren();
}
