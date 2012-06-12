package de.proskor.fel;

import java.util.List;

import de.proskor.fel.container.EventTypeContainer;
import de.proskor.fel.event.EventType;

public interface EventRepository {
	public List<EventType> getEventTypes();
	
//	public boolean writeEventType(EventType e); 
//	public boolean writeEventInstance(EventType e);
	
	public List<EventTypeContainer> getEventTypeContainers();

//	public EventInstanceContainer getEventInstanceContainer(Element element);
	
//	public void reloadFromEA();
}