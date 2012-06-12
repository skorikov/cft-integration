package de.proskor.fel.container;

import java.util.List;

import de.proskor.fel.Instance;
import de.proskor.fel.event.EventInstance;

public interface EventInstanceContainer extends EventContainer, Instance {
	@Override
	public EventTypeContainer getType();
	
	@Override
	public List<EventInstance> getEvents();
	
	public void addEvent(EventInstance event);
}