package de.proskor.fel;

import java.util.List;

import de.proskor.fel.container.EventTypeContainer;

public interface EventRepository {
	public List<EventTypeContainer> getEventTypeContainers();
}