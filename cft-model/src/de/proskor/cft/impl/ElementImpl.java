package de.proskor.cft.impl;

import de.proskor.cft.Element;

public abstract class ElementImpl implements Element {
	private final de.proskor.model.Element peer;

	public ElementImpl(de.proskor.model.Element peer) {
		if (peer == null)
			throw new IllegalArgumentException("peer is null");

		this.peer = peer;
	}

	@Override
	public String getName() {
		return this.getPeer().getName();
	}

	@Override
	public void setName(String name) {
		this.getPeer().setName(name);
	}

	protected final de.proskor.model.Element getPeer() {
		return this.peer;
	}
}
