package de.proskor.fel.ui;

import de.proskor.fel.model.EventRepository;
import de.proskor.fel.ui.impl.FailureEventListImpl;

public abstract class FailureEventListFactory {
	public static FailureEventList createGUI(EventRepository repository) {
		return new FailureEventListImpl(repository);
	}
}
