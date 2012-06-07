package de.proskor.fel;

import java.util.List;

public interface EventInstanceContainer {
	public String getName();
	public List<EventInstance> getEventInstances();
	public void addEvent(EventInstance instance);
}