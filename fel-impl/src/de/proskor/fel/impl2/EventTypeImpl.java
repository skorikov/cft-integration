package de.proskor.fel.impl2;

import java.util.Collections;
import java.util.List;

import de.proskor.fel.container.EventTypeContainer;
import de.proskor.fel.event.EventInstance;
import de.proskor.fel.event.EventType;
import de.proskor.model.Element;

class EventTypeImpl extends EntityImpl implements EventType {
	EventTypeImpl(Element peer) {
		super(peer);
	}

	@Override
	public List<EventInstance> getInstances() {
		// TODO Auto-generated method stub
		return Collections.emptyList();
	}

	@Override
	public void addInstance(EventInstance eventInstance) {
		// TODO Auto-generated method stub

	}

	@Override
	public EventTypeContainer getContainer() {
		// TODO Auto-generated method stub
		return null;
	}

}
