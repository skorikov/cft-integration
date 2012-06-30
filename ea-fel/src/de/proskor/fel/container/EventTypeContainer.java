package de.proskor.fel.container;

import java.util.List;

import de.proskor.fel.Type;
import de.proskor.fel.event.EventType;

public interface EventTypeContainer extends EventContainer, Type {
	@Override
	public List<EventType> getEvents();
	
	@Override
	public List<EventInstanceContainer> getInstances();
	
	public EventType createEventType(String name);
	
	public void addInstance(EventInstanceContainer instance);
	
	@Override
	public EventTypeContainer getParent();
	@Override
	public List<EventTypeContainer> getChildren();
}
