package de.proskor.fel.model.impl;

import de.proskor.fel.model.Entity;
import de.proskor.model.Element;
import de.proskor.model.Repository;

abstract class EntityImpl implements Entity {
	private final Repository repository;
	private final Element peer;

	protected EntityImpl(Repository repository, Element peer) {
		this.repository = repository;
		this.peer = peer;
	}

	protected final Repository getRepository() {
		return this.repository;
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
		return this.getQN(this.peer);
	}

	private String getQN(Element element) {
		final String name = element.getName();
		final String parent = element.isChild() ? this.getQN(element.getParent()) + "." : "";
		return parent + name;
	}

	@Override
	public boolean equals(Object that) {
		if (that == null)
			return false;

		if (that instanceof EntityImpl)
			return this.peer.equals(((EntityImpl) that).peer);

		return false;
	}

	@Override
	public int hashCode() {
		return this.peer.hashCode();
	}
}
