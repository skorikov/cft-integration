package de.proskor.fel.model.impl;

import de.proskor.fel.model.view.View;
import de.proskor.model.Diagram;
import de.proskor.model.Repository;

class ViewImpl implements View {
	private final Repository repository;
	private final Diagram peer;

	public ViewImpl(Repository repository, Diagram peer) {
		this.repository = repository;
		this.peer = peer;
	}

	protected final Repository getRepository() {
		return this.repository;
	}

	protected final Diagram getPeer() {
		return this.peer;
	}

	@Override
	public String getName() {
		return this.peer.getName();
	}
}
