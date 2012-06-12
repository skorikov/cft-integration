package de.proskor.fel.impl;

import de.proskor.fel.Entity;

abstract class EntityImpl implements Entity {
	private String name;
	private String guid;
	private int id;

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public String getGuid() {
		return guid;
	}
	
	public EntityImpl(String name, String guid, int id) {
		this.name = name;
		this.guid = guid;
		this.id = id;
	}
}
