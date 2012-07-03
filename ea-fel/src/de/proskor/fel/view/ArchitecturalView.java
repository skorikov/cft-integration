package de.proskor.fel.view;

import java.util.List;

import de.proskor.fel.container.EventTypeContainer;

public interface ArchitecturalView extends View {
	public List<EventTypeContainer> getEventTypeContainers();
}
