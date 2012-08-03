package de.proskor.fel.model.impl;

import java.util.LinkedList;
import java.util.List;

import de.proskor.fel.model.EventRepository;
import de.proskor.fel.model.container.EventTypeContainer;
import de.proskor.fel.model.view.View;
import de.proskor.fel.model.view.ViewRepository;
import de.proskor.model.Diagram;
import de.proskor.model.Element;
import de.proskor.model.Package;
import de.proskor.model.Repository;

public class EventRepositoryImpl implements EventRepository, ViewRepository {
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

	private List<Diagram> getAllDiagrams() {
		final List<Diagram> result = new LinkedList<Diagram>();

		for (final Package model : this.peer.getModels())
			result.addAll(this.getAllDiagrams(model));

		return result;
	}

	private List<Diagram> getAllDiagrams(Package pkg) {
		final List<Diagram> result = new LinkedList<Diagram>();

		for (final Diagram diagram : pkg.getDiagrams())
			result.add(diagram);

		for (final Package kid : pkg.getPackages())
			result.addAll(this.getAllDiagrams(kid));

		return result;
	}

	@Override
	public List<? extends View> getViews() {
		final List<View> result = new LinkedList<View>();
		for (final Diagram diagram : this.getAllDiagrams()) {
			if (diagram.getStereotype().equals("CFT")) {
				result.add(new ComponentFaultTreeImpl(peer, diagram));
			} else {
				result.add(new ArchitecturalViewImpl(peer, diagram));
			}
		}
		return result;
	}
}
