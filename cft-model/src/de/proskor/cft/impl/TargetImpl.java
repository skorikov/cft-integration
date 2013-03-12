package de.proskor.cft.impl;

import java.util.Collection;

import de.proskor.cft.Source;
import de.proskor.cft.Target;
import de.proskor.model.Element;

public class TargetImpl extends SourceImpl implements Target {
	public TargetImpl(Element peer) {
		super(peer);
	}

	@Override
	public Collection<Source> getInputs() {
		// TODO Auto-generated method stub
		return null;
	}
}
