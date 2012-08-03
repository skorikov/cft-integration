package de.proskor.fel.model.view;

import java.util.List;

import de.proskor.fel.model.container.EventTypeContainer;

public interface ArchitecturalView extends View {
	public List<EventTypeContainer> getEventTypeContainers();
}
