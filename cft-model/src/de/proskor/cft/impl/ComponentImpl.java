package de.proskor.cft.impl;

import java.util.Collection;

import de.proskor.cft.Component;
import de.proskor.cft.Container;
import de.proskor.cft.Element;
import de.proskor.cft.Event;
import de.proskor.cft.Gate;
import de.proskor.cft.Inport;
import de.proskor.cft.Outport;

public class ComponentImpl extends ElementImpl implements Component {
	public ComponentImpl(de.proskor.model.Element peer) {
		super(peer);
	}

	@Override
	public Collection<Element> getElements() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Container getParent() {
		final de.proskor.model.Element peer = this.getPeer();
		if (peer.isChild()) {
			return new ComponentImpl(peer.getParent());
		} else {
			return new PackageImpl(peer.getPackage());
		}
	}

	@Override
	public Collection<Event> getEvents() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Inport> getInports() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Outport> getOutports() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Gate> getGates() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Component> getComponents() {
		// TODO Auto-generated method stub
		return null;
	}

}
