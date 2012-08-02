package de.proskor.fel.impl.stub;

import java.util.ArrayList;
import java.util.List;

import de.proskor.fel.container.EventInstanceContainer;
import de.proskor.fel.container.EventTypeContainer;
import de.proskor.fel.event.EventInstance;

public class EventInstanceContainerImpl extends EntityImpl implements EventInstanceContainer {
	private EventTypeContainer type;
	private List<EventInstance> eventInstances = new ArrayList<EventInstance>();
	private List<EventInstanceContainer> children = new ArrayList<EventInstanceContainer>();
	private EventInstanceContainer parent;

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
	public String getQualifiedName() {
		if (parent != null)
			return parent.getQualifiedName() + "." + getName();
		else
			return getName();
	}

	@Override
	public EventInstanceContainer getParent() {
		return parent;
	}

	@Override
	public List<EventInstanceContainer> getChildren() {
		return children;
	}
}
