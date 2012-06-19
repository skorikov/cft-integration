package de.proskor.fel.event;

import de.proskor.fel.Instance;
import de.proskor.fel.container.EventInstanceContainer;

public interface EventInstance extends Instance, Event {
	@Override 
	public EventInstanceContainer getContainer();
	
	@Override 
	public EventType getType();

	public void setType(EventType typ);
}