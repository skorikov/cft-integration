package de.proskor.cft.impl;

import de.proskor.cft.Component;
import de.proskor.cft.Source;
import de.proskor.model.Element;

public abstract class SourceImpl extends ElementImpl implements Source {
	public SourceImpl(Element peer) {
		super(peer);
	}

	@Override
	public Component getParent() {
		return new ComponentImpl(this.getPeer().getParent());
	}
}
