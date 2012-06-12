package de.proskor.fel.event;

import java.util.List;

import de.proskor.fel.Type;
import de.proskor.fel.container.EventTypeContainer;

public interface EventType extends Type, Event {
	@Override
	public List<EventInstance> getInstances();
	
	public void addInstance(EventInstance eventInstance);
	
	@Override
	public EventTypeContainer getContainer();
}