package de.proskor.fel.impl;

import java.util.ArrayList;
import java.util.List;

import de.proskor.fel.EventInstanceContainer;
import de.proskor.fel.EventInstance;

public class EventContainerImpl implements EventInstanceContainer {
	private String name;
	private List<EventInstance> eventInstances;
	
	@Override
	public List<EventInstance> getEventInstances() {
		return eventInstances;
	}

	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public void addEvent(EventInstance instance) {
		eventInstances.add(instance);
	}

	public EventContainerImpl(String name) {
		this.name = name;
		eventInstances = new ArrayList<EventInstance>();
	}
}