package de.proskor.fel.impl2;

import de.proskor.fel.container.EventInstanceContainer;
import de.proskor.fel.event.EventInstance;
import de.proskor.fel.event.EventType;
import de.proskor.model.Element;

class EventInstanceImpl extends EntityImpl implements EventInstance {
	EventInstanceImpl(Element peer) {
		super(peer);
	}

	@Override
	public EventInstanceContainer getContainer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EventType getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setType(EventType typ) {
		// TODO Auto-generated method stub

	}
}
