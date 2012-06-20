package de.proskor.fel.stub;

import java.util.ArrayList;
import java.util.List;

import de.proskor.fel.container.EventInstanceContainer;
import de.proskor.fel.container.EventTypeContainer;
import de.proskor.fel.event.EventType;

public class EventTypeContainerImpl extends EntityImpl implements EventTypeContainer {
	private List<EventType> eventTypes = new ArrayList<EventType>();
	private List<EventInstanceContainer> instances = new ArrayList<EventInstanceContainer>();

	@Override
	public List<EventType> getEvents() {
		return this.eventTypes;
	}

	@Override
	public List<EventInstanceContainer> getInstances() {
		return this.instances;
	}

	@Override
	public EventType createEvent(String name) {
		EventTypeImpl eventType = new EventTypeImpl();
		eventType.setName(name);
		eventType.setContainer(this);
		this.eventTypes.add(eventType);
		return eventType;
	}

	@Override
	public void addInstance(EventInstanceContainer instance) {
		this.instances.add(instance);
	}
}
