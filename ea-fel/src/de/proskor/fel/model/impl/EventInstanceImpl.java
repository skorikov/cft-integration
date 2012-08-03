package de.proskor.fel.model.impl;

import de.proskor.fel.model.container.EventInstanceContainer;
import de.proskor.fel.model.event.EventInstance;
import de.proskor.fel.model.event.EventType;
import de.proskor.model.Connector;
import de.proskor.model.Element;
import de.proskor.model.Repository;

class EventInstanceImpl extends EntityImpl implements EventInstance {
	EventInstanceImpl(Repository repository, Element peer) {
		super(repository, peer);
	}

	@Override
	public EventInstanceContainer getContainer() {
		final Element peer = this.getPeer();
		return new EventInstanceContainerImpl(this.getRepository(), peer.getParent());
	}

	@Override
	public EventType getType() {
		final Element peer = this.getPeer();
		for (final Connector connector : peer.getConnectors()) {
			if (connector.getStereotype().equals("instanceOf"))
				return new EventTypeImpl(this.getRepository(), connector.getTarget());
		}
		return null;
	}

	@Override
	public void setType(EventType typ) {
		// TODO Auto-generated method stub
	}
}
