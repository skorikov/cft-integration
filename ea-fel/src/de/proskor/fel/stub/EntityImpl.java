package de.proskor.fel.stub;

import de.proskor.fel.Entity;

public abstract class EntityImpl implements Entity {
	private static int globalId = 0;

	private int id;
	private String guid;
	private String name;
	private String author;
	private String description;

	protected static int generateId() {
		return EntityImpl.globalId++;
	}

	protected EntityImpl() {
		this.id = EntityImpl.generateId();
		this.guid = String.valueOf(this.id);
	}

	@Override
	public int getId() {
		return this.id;
	}

	@Override
	public String getGuid() {
		return this.guid;
	}

	@Override
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getDescription() {
		return this.description;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String getAuthor() {
		return this.author;
	}

	@Override
	public void setAuthor(String author) {
		this.author = author;
	}
}
