package de.proskor.fel.stub;

import java.util.ArrayList;
import java.util.List;

import de.proskor.fel.container.EventInstanceContainer;
import de.proskor.fel.container.EventTypeContainer;
import de.proskor.fel.event.EventType;

public class EventTypeContainerImpl extends EntityImpl implements EventTypeContainer {
	private List<EventType> eventTypes = new ArrayList<EventType>();
	private List<EventInstanceContainer> instances = new ArrayList<EventInstanceContainer>();
	private final EventTypeContainer parent;
	private ArrayList<EventTypeContainer> children = new ArrayList<EventTypeContainer>();

	public EventTypeContainerImpl() {
		parent = null; 
	}
	
	public EventTypeContainerImpl(EventTypeContainer parent) {
		this.parent = parent; 
		parent.getChildren().add(this);
	}
	
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
		((EventInstanceContainerImpl)instance).setType(this);
	}

	@Override
	public EventTypeContainer getParent() {
		return parent;
	}

	@Override
	public List<EventTypeContainer> getChildren() {
		return children;
	}
}
