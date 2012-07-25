package de.proskor.fel.impl2;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import de.proskor.fel.container.EventInstanceContainer;
import de.proskor.fel.container.EventTypeContainer;
import de.proskor.fel.event.EventType;
import de.proskor.model.Connector;
import de.proskor.model.Element;

class EventTypeContainerImpl extends EntityImpl implements EventTypeContainer {
	EventTypeContainerImpl(Element peer) {
		super(peer);
	}

	@Override
	public List<EventType> getEvents() {
		final Element peer = this.getPeer();
		final List<EventType> result = new LinkedList<EventType>();
		for (final Connector connector : peer.getConnectors()) {
			if (connector.getTarget().equals(peer) && connector.getStereotype().equals("belongsTo")) {
				final Element element = connector.getSource();
				final EventType event = new EventTypeImpl(element);
				result.add(event);
			}
		}
		return result;
	}

	@Override
	public List<EventInstanceContainer> getInstances() {
		final Element peer = this.getPeer();
		final List<EventInstanceContainer> result = new LinkedList<EventInstanceContainer>();
		for (final Connector connector : peer.getConnectors()) {
			if (connector.getTarget().equals(peer) && connector.getStereotype().equals("instanceOf")) {
				final Element element = connector.getSource();
				final EventInstanceContainer container = new EventInstanceContainerImpl(element);
				result.add(container);
			}
		}
		return result;
	}

	@Override
	public EventType createEventType(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addInstance(EventInstanceContainer instance) {
		// TODO Auto-generated method stub

	}

	@Override
	public EventTypeContainer getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EventTypeContainer> getChildren() {
		// TODO Auto-generated method stub
		return Collections.emptyList();
	}
}
