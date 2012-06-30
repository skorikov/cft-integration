package de.proskor.fel.container;

import java.util.List;

import de.proskor.fel.event.Event;

public interface EventContainer {
	public List<? extends Event> getEvents();
	public String getName();
	public Event createEvent(String name);
	
	public EventTypeContainer getParent();
	public List<EventTypeContainer> getChildren();
}
