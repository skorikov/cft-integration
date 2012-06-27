package de.proskor.fel.container;

import java.util.List;

import de.proskor.fel.Type;
import de.proskor.fel.event.EventType;

public interface EventTypeContainer extends EventContainer, Type {
	@Override
	public List<EventType> getEvents();
	
	@Override
	public List<EventInstanceContainer> getInstances();
	
	@Override
	@Deprecated
	/**
	 * Soll in createEventType o.ä. umbenannt werden.
	 */
	public EventType createEvent(String name);
	
	public void addInstance(EventInstanceContainer instance);
}
