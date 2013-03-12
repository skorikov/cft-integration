package de.proskor.cft.impl;

import de.proskor.cft.Port;
import de.proskor.model.Element;

public abstract class PortImpl extends TargetImpl implements Port {
	public PortImpl(Element peer) {
		super(peer);
	}
}
