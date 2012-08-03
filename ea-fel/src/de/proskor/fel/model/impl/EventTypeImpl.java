package de.proskor.fel.model.impl;

import java.util.LinkedList;
import java.util.List;

import de.proskor.fel.model.container.EventTypeContainer;
import de.proskor.fel.model.event.EventInstance;
import de.proskor.fel.model.event.EventType;
import de.proskor.model.Connector;
import de.proskor.model.Element;
import de.proskor.model.Repository;

class EventTypeImpl extends EntityImpl implements EventType {
	EventTypeImpl(Repository repository, Element peer) {
		super(repository, peer);
	}

	@Override
	public List<EventInstance> getInstances() {
		final List<EventInstance> result = new LinkedList<EventInstance>();
		final Element peer = this.getPeer();
		for (final Connector connector : peer.getConnectors()) {
			if (connector.getStereotype().equals("instanceOf")) {
				final Element element = connector.getSource();
				final EventInstance ei = new EventInstanceImpl(this.getRepository(), element);
				result.add(ei);
			}
		}
		return result;
	}

	@Override
	public void addInstance(EventInstance eventInstance) {
		if (!(eventInstance instanceof EventInstanceImpl))
			throw new IllegalArgumentException();

		final Element peer = this.getPeer();
		final Element other = ((EventInstanceImpl) eventInstance).getPeer();

		final Connector connector = other.connectTo(peer);
		connector.setStereotype("instanceOf");
	}

	@Override
	public EventTypeContainer getContainer() {
		final Element peer = this.getPeer();
		for (final Connector connector : peer.getConnectors()) {
			if (connector.getStereotype().equals("belongsTo"))
				return new EventTypeContainerImpl(this.getRepository(), connector.getTarget());
		}
		return null;
	}
}
