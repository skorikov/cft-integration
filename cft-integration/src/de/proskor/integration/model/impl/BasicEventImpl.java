package de.proskor.integration.model.impl;

import de.proskor.integration.model.BasicEvent;
import de.proskor.integration.model.EventType;

public class BasicEventImpl implements BasicEvent {
	private String name;
	private EventType type;

	public BasicEventImpl(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public EventType getType() {
		return this.type;
	}

	@Override
	public void setType(EventType type) {
		this.type = type;
	}
}
