package de.proskor.fel.container;

import java.util.List;

import de.proskor.fel.event.Event;

public interface EventContainer {
	public List<? extends Event> getEvents();
	public String getName();

	@Deprecated
	/**
	 * Soll weggehen. Bei EventType in createEventType o.ä. umbenennen.
	 * @param name
	 * @return
	 */
	public Event createEvent(String name);
}
