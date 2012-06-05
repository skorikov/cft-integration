package de.proskor.fel.impl;

import java.util.ArrayList;
import java.util.List;

import de.proskor.fel.EventCFT;
import de.proskor.fel.EventInstance;

public class EventCFTImpl implements EventCFT {
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

	public EventCFTImpl(String name) {
		this.name = name;
		eventInstances = new ArrayList<EventInstance>();
	}
}