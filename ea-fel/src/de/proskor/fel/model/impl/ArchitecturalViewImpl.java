package de.proskor.fel.model.impl;

import java.util.LinkedList;
import java.util.List;

import de.proskor.fel.model.container.EventTypeContainer;
import de.proskor.fel.model.view.ArchitecturalView;
import de.proskor.model.Diagram;
import de.proskor.model.Element;
import de.proskor.model.Node;
import de.proskor.model.Repository;

public class ArchitecturalViewImpl extends ViewImpl implements ArchitecturalView {
	public ArchitecturalViewImpl(Repository repository, Diagram peer) {
		super(repository, peer);
	}

	@Override
	public List<EventTypeContainer> getEventTypeContainers() {
		final Diagram peer = this.getPeer();
		final List<EventTypeContainer> result = new LinkedList<EventTypeContainer>();
		for (final Node node : peer.getNodes()) {
			final Element element = node.getElement();
			if (element.getStereotype().equals("EventTypeContainer"))
				result.add(new EventTypeContainerImpl(this.getRepository(), element));
		}
		return result;
	}
}
