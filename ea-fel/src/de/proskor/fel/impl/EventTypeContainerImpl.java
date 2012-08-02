package de.proskor.fel.impl;

import java.util.LinkedList;
import java.util.List;

import de.proskor.fel.container.EventInstanceContainer;
import de.proskor.fel.container.EventTypeContainer;
import de.proskor.fel.event.EventType;
import de.proskor.model.Connector;
import de.proskor.model.Element;
import de.proskor.model.Package;
import de.proskor.model.Repository;

class EventTypeContainerImpl extends EntityImpl implements EventTypeContainer {
	EventTypeContainerImpl(Repository repository, Element peer) {
		super(repository, peer);
	}

	@Override
	public List<EventType> getEvents() {
		final Element peer = this.getPeer();
		final List<EventType> result = new LinkedList<EventType>();
		for (final Connector connector : peer.getConnectors()) {
			if (connector.getTarget().equals(peer) && connector.getStereotype().equals("belongsTo")) {
				final Element element = connector.getSource();
				final EventType event = new EventTypeImpl(this.getRepository(), element);
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
				final EventInstanceContainer container = new EventInstanceContainerImpl(this.getRepository(), element);
				result.add(container);
			}
		}
		return result;
	}

	@Override
	public EventType createEventType(String name) {
		final Element peer = this.getPeer();
		final Package fel = this.getFEL();
		final Element element = fel.getElements().add(name, Element.OBJECT);
		element.setStereotype("EventType");
		final Connector connector = element.connectTo(peer);
		connector.setStereotype("belongsTo");
		return new EventTypeImpl(this.getRepository(), element);
	}

	private Package getFEL() {
		final Repository repository = this.getRepository();
		final Package model = repository.getModels().get(0);
		for (final Package pkg : model.getPackages()) {
			if (pkg.getName().equals("FEL"))
				return pkg;
		}
		return model.getPackages().add("FEL", Package.PACKAGE);
	}

	@Override
	public void addInstance(EventInstanceContainer instance) {
		if (!(instance instanceof EventInstanceContainerImpl))
			throw new IllegalArgumentException();

		final Element peer = this.getPeer();
		final Element other = ((EventInstanceContainerImpl) instance).getPeer();

		final Connector connector = other.connectTo(peer);
		connector.setStereotype("instanceOf");
	}

	@Override
	public EventTypeContainer getParent() {
		final Element peer = this.getPeer();

		if (!peer.isChild())
			return null;

		final Element parent = peer.getParent();
		if (parent.getStereotype().equals("EventTypeContainer"))
			return new EventTypeContainerImpl(this.getRepository(), parent);

		return null;
	}

	@Override
	public List<EventTypeContainer> getChildren() {
		final Element peer = this.getPeer();
		final List<EventTypeContainer> result = new LinkedList<EventTypeContainer>();
		for (final Element kid : peer.getElements()) {
			if (kid.getStereotype().equals("EventTypeContainer")) {
				final EventTypeContainer etc = new EventTypeContainerImpl(this.getRepository(), kid);
				result.add(etc);
			}
		}
		return result;
	}
}
