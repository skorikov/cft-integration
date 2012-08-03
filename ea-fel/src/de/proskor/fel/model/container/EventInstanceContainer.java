package de.proskor.fel.model.container;

import java.util.List;

import de.proskor.fel.model.Instance;
import de.proskor.fel.model.event.EventInstance;

public interface EventInstanceContainer extends EventContainer, Instance {
	@Override
	public EventTypeContainer getType();
	
	@Override
	public List<EventInstance> getEvents();

	@Override
	public EventInstanceContainer getParent();
	@Override
	public List<EventInstanceContainer> getChildren();
}