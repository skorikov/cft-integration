package de.proskor.fel.model.event;

import de.proskor.fel.model.Instance;
import de.proskor.fel.model.container.EventInstanceContainer;

public interface EventInstance extends Instance, Event {
	@Override 
	public EventInstanceContainer getContainer();
	
	@Override 
	public EventType getType();

	public void setType(EventType typ);
}