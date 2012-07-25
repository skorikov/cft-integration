package de.proskor.fel.impl2;

import java.util.Collections;
import java.util.List;

import de.proskor.fel.container.EventInstanceContainer;
import de.proskor.fel.container.EventTypeContainer;
import de.proskor.fel.event.EventInstance;
import de.proskor.model.Element;

class EventInstanceContainerImpl extends EntityImpl implements EventInstanceContainer {
	EventInstanceContainerImpl(Element peer) {
		super(peer);
	}

	@Override
	public EventTypeContainer getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EventInstance> getEvents() {
		// TODO Auto-generated method stub
		return Collections.emptyList();
	}

	@Override
	public EventInstanceContainer getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EventInstanceContainer> getChildren() {
		// TODO Auto-generated method stub
		return Collections.emptyList();
	}
}
