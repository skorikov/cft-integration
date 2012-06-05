package de.proskor.fel.impl;

import java.util.ArrayList;

public class EventCFT {
	private String name;
	private ArrayList<EventInstance> eventInstances;
	
	public ArrayList<EventInstance> getEventInstances() {
		return eventInstances;
	}

	public String getName() {
		return name;
	}
	
	protected void addEvent(EventInstance instance) {
		eventInstances.add(instance);
	}

	public EventCFT(String name) {
		this.name = name;
		eventInstances = new ArrayList<EventInstance>();
	}
}