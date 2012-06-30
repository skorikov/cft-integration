package de.proskor.fel.stub;

import java.util.ArrayList;
import java.util.List;

import de.proskor.fel.EventRepository;
import de.proskor.fel.container.EventTypeContainer;

public class EventRepositoryImpl implements EventRepository {
	private List<EventTypeContainer> containers = new ArrayList<EventTypeContainer>();

	@Override
	public List<EventTypeContainer> getEventTypeContainers() {
		return this.containers;
	}
}
