package de.proskor.fel.impl2;

import java.util.LinkedList;
import java.util.List;

import de.proskor.fel.container.EventInstanceContainer;
import de.proskor.fel.container.EventTypeContainer;
import de.proskor.fel.event.EventInstance;
import de.proskor.model.Connector;
import de.proskor.model.Element;
import de.proskor.model.Repository;

class EventInstanceContainerImpl extends EntityImpl implements EventInstanceContainer {
	EventInstanceContainerImpl(Repository repository, Element peer) {
		super(repository, peer);
	}

	@Override
	public EventTypeContainer getType() {
		final Element peer = this.getPeer();
		for (final Connector connector : peer.getConnectors()) {
			if (connector.getStereotype().equals("instanceOf"))
				return new EventTypeContainerImpl(this.getRepository(), connector.getTarget());
		}
		return null;
	}

	@Override
	public List<EventInstance> getEvents() {
		final Element peer = this.getPeer();
		final List<EventInstance> result = new LinkedList<EventInstance>();
		for (final Element kid : peer.getElements()) {
			if (kid.getStereotype().equals("Event")) {
				final EventInstance ei = new EventInstanceImpl(this.getRepository(), kid);
				result.add(ei);
			}
		}
		return result;
	}

	@Override
	public EventInstanceContainer getParent() {
		final Element peer = this.getPeer();

		if (!peer.isChild())
			return null;

		final Element parent = peer.getParent();
		if (parent.getStereotype().equals("Component"))
			return new EventInstanceContainerImpl(this.getRepository(), parent);

		return null;
	}

	@Override
	public List<EventInstanceContainer> getChildren() {
		final Element peer = this.getPeer();
		final List<EventInstanceContainer> result = new LinkedList<EventInstanceContainer>();
		for (final Element kid : peer.getElements()) {
			if (kid.getStereotype().equals("Component")) {
				final EventInstanceContainer eic = new EventInstanceContainerImpl(this.getRepository(), kid);
				result.add(eic);
			}
		}
		return result;
	}
}
