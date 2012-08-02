package de.proskor.fel.impl;

import java.util.LinkedList;
import java.util.List;

import de.proskor.fel.EventRepository;
import de.proskor.fel.container.EventTypeContainer;
import de.proskor.model.Element;
import de.proskor.model.Package;
import de.proskor.model.Repository;

public class EventRepositoryImpl implements EventRepository {
	private final Repository peer;

	public EventRepositoryImpl(Repository peer) {
		this.peer = peer;
	}

	@Override
	public List<EventTypeContainer> getEventTypeContainers() {
		final List<EventTypeContainer> result = new LinkedList<EventTypeContainer>();

		for (final Element element : this.getAllElements()) {
			final EventTypeContainer etc = new EventTypeContainerImpl(this.peer, element);
			result.add(etc);
		}

		return result;
	}

	private List<Element> getAllElements() {
		final List<Element> result = new LinkedList<Element>();

		for (final Package pkg : this.peer.getModels()) {
			result.addAll(this.getAllElements(pkg));
		}

		return result;
	}

	private List<Element> getAllElements(Package pkg) {
		final List<Element> result = new LinkedList<Element>();

		for (final Package kid : pkg.getPackages()) {
			result.addAll(this.getAllElements(kid));
		}

		for (final Element kid : pkg.getElements()) {
			result.addAll(this.getAllElements(kid));
		}

		return result;
	}

	private List<Element> getAllElements(Element element) {
		final List<Element> result = new LinkedList<Element>();

		if (element.getStereotype().equals("EventTypeContainer"))
			result.add(element);

//		TODO: Check protocol!
//		for (final Element kid : element.getElements()) {
//			result.addAll(this.getAllElements(kid));
//		}

		return result;
	}
}
