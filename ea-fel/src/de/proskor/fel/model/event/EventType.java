package de.proskor.fel.model.event;

import java.util.List;

import de.proskor.fel.model.Type;
import de.proskor.fel.model.container.EventTypeContainer;

public interface EventType extends Type, Event {
	@Override
	public List<EventInstance> getInstances();
	
	public void addInstance(EventInstance eventInstance);
	
	@Override
	public EventTypeContainer getContainer();
}