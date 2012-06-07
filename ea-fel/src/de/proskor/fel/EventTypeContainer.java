package de.proskor.fel;

import java.util.List;

public interface EventTypeContainer {
	public String getName();
	public List<EventType> getEventTypes();
	public void addEventType(EventType et);
}
