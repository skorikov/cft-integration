package de.proskor.integration.model.impl;

import de.proskor.integration.model.BasicEvent;

public class BasicEventImpl implements BasicEvent {
	private String name;

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
}
