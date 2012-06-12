package de.proskor.fel.impl;

import java.util.ArrayList;
import java.util.List;

import de.proskor.fel.container.EventInstanceContainer;
import de.proskor.fel.container.EventTypeContainer;
import de.proskor.fel.event.Event;
import de.proskor.fel.event.EventInstance;

public class EventInstanceContainerImpl extends EntityImpl implements EventInstanceContainer {
	private List<EventInstance> eventInstances;
	private final EventTypeContainer typeContainer;
	
	@Override
	public List<EventInstance> getEvents() {
		return eventInstances;
	}

	@Override
	public void addEvent(EventInstance instance) {
		if(!eventInstances.contains(instance))
			eventInstances.add(instance);
	}

	public EventInstanceContainerImpl(String name, String guid, int id, EventTypeContainer typeContainer) {
		super(name, guid, id);
		
		this.typeContainer = typeContainer;
		eventInstances = new ArrayList<EventInstance>();
		
		typeContainer.addInstance(this);
	}

	@Override
	public EventTypeContainer getType() {
		return typeContainer;
	}

}