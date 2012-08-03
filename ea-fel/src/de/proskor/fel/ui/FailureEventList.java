package de.proskor.fel.ui;

import de.proskor.fel.model.EventRepository;
import de.proskor.fel.model.container.EventTypeContainer;
import de.proskor.fel.model.event.EventType;

public interface FailureEventList {
	public void showDialog();
	public void showDialog(EventTypeContainer selectedContainer);
	public void showDialog(EventType selectedEvent);
	
	public EventRepository getRepository();
}
