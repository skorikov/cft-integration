package de.proskor.fel.model;

import java.util.List;

import de.proskor.fel.model.container.EventTypeContainer;

public interface EventRepository {
	public List<EventTypeContainer> getEventTypeContainers();
}