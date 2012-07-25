package de.proskor.fel.impl2;

import de.proskor.fel.Entity;
import de.proskor.model.Element;

abstract class EntityImpl implements Entity {
	private final Element peer;

	protected EntityImpl(Element peer) {
		this.peer = peer;
	}

	protected final Element getPeer() {
		return this.peer;
	}

	@Override
	public String getName() {
		return this.peer.getName();
	}

	@Override
	public int getId() {
		return this.peer.getId();
	}

	@Override
	public String getGuid() {
		return this.peer.getGuid();
	}

	@Override
	public String getDescription() {
		return this.peer.getDescription();
	}

	@Override
	public String getAuthor() {
		return this.peer.getAuthor();
	}

	@Override
	public void setDescription(String description) {
		this.peer.setDescription(description);
	}

	@Override
	public void setAuthor(String author) {
		this.peer.setAuthor(author);
	}

	@Override
	public String getQualifiedName() {
		return "TODO";
	}
}
