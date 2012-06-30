package de.proskor.fel.ui;

import de.proskor.fel.EventRepository;
import de.proskor.fel.container.EventTypeContainer;
import de.proskor.fel.event.EventType;

public interface FailureEventList {
	public void showDialog();
	public void showDialog(EventTypeContainer selectedContainer);
	public void showDialog(EventType selectedEvent);
	
	public EventRepository getRepository();
}
