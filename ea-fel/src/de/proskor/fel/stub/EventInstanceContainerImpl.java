package de.proskor.fel.stub;

import java.util.ArrayList;
import java.util.List;

import de.proskor.fel.container.EventInstanceContainer;
import de.proskor.fel.container.EventTypeContainer;
import de.proskor.fel.event.EventInstance;

public class EventInstanceContainerImpl extends EntityImpl implements EventInstanceContainer {
	private EventTypeContainer type;
	private List<EventInstance> eventInstances = new ArrayList<EventInstance>();
	private List<EventTypeContainer> children = new ArrayList<EventTypeContainer>();
	private EventTypeContainer parent;

	public EventTypeContainer getTyp() {
		return type;
	}

	public void setType(EventTypeContainer typ) {
		this.type = typ;
	}

	@Override
	public EventTypeContainer getType() {
		return this.type;
	}

	@Override
	public List<EventInstance> getEvents() {
		return this.eventInstances;
	}

	@Override
	public EventInstance createEvent(String name) {
		EventInstanceImpl event = new EventInstanceImpl();
		event.setName(name);
		event.setContainer(this);
		this.eventInstances.add(event);
		return event;
	}

	@Override
	public String getQualifiedName() {
		if (parent != null)
			return parent.getQualifiedName() + "." + getName();
		else
			return getName();
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
