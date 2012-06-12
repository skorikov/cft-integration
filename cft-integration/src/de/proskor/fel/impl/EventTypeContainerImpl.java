package de.proskor.fel.impl;

import java.util.ArrayList;
import java.util.List;

import de.proskor.fel.container.EventInstanceContainer;
import de.proskor.fel.container.EventTypeContainer;
import de.proskor.fel.event.EventType;

public class EventTypeContainerImpl extends EntityImpl implements EventTypeContainer {
	private ArrayList<EventType> eventTypes;
	private ArrayList<EventInstanceContainer> instanceContainers;
	
	public EventTypeContainerImpl(String name, String guid, int id) {
		super(name, guid, id);
		
		eventTypes = new ArrayList<EventType>();
		instanceContainers = new ArrayList<EventInstanceContainer>();
	}

	@Override
	public List<EventType> getEvents() {
		return eventTypes;
	}

	@Override
	public List<EventInstanceContainer> getInstances() {
		return instanceContainers;
	}

	@Override
	public void addEvent(EventType et) {
		eventTypes.add(et);	
	}

	@Override
	public void addInstance(EventInstanceContainer instance) {
		if (!instanceContainers.contains(instance))
			instanceContainers.add(instance);
	}
}