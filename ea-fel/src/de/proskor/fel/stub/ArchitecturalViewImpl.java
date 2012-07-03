package de.proskor.fel.stub;

import java.util.ArrayList;
import java.util.List;

import de.proskor.fel.container.EventTypeContainer;
import de.proskor.fel.view.ArchitecturalView;

public class ArchitecturalViewImpl extends ViewImpl implements ArchitecturalView {
	private List<EventTypeContainer> components = new ArrayList<EventTypeContainer>();

	@Override
	public List<EventTypeContainer> getEventTypeContainers() {
		return this.components;
	}
}
