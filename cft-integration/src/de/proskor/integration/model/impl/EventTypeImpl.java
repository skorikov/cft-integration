package de.proskor.integration.model.impl;

import de.proskor.integration.model.EventType;

public class EventTypeImpl implements EventType {
	private String name;

	public EventTypeImpl(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return this.name;
	}
}
