package de.proskor.fel;

import java.util.ArrayList;
import java.util.List;

import de.proskor.fel.container.EventTypeContainer;
import de.proskor.fel.event.EventType;

public interface EventRepository {
	public ArrayList<EventType> getEventTypes();
	
	public boolean writeEventType(EventType e); 
	public boolean writeEventInstance(EventType e);
	
	public List<EventTypeContainer> getEventTypeContainers();
	
	public void reloadFromEA();
}