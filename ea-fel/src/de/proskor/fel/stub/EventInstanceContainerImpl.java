package de.proskor.fel.stub;

import java.util.List;

import de.proskor.fel.container.EventInstanceContainer;
import de.proskor.fel.container.EventTypeContainer;
import de.proskor.fel.event.EventInstance;

public class EventInstanceContainerImpl extends EntityImpl implements EventInstanceContainer {
	private EventTypeContainer typ;
	private List<EventInstance> eventInstances;

	@Override
	public EventTypeContainer getType() {
		return this.typ;
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
}
