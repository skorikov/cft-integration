package de.proskor.fel;

import java.util.List;

public interface EventCFT {
	public String getName();
	public List<EventInstance> getEventInstances();
	public void addEvent(EventInstance instance);
}