package de.proskor.cft.impl;

import java.util.Collection;

import de.proskor.cft.Component;
import de.proskor.cft.Container;
import de.proskor.cft.Element;
import de.proskor.cft.Event;
import de.proskor.cft.Gate;
import de.proskor.cft.Inport;
import de.proskor.cft.Outport;

public class ComponentImpl implements Component {
	private final static String STEREOTYPE = "component";
	private final de.proskor.model.Element peer;

	public ComponentImpl(de.proskor.model.Element peer) {
		if (peer == null)
			throw new IllegalArgumentException("peer is null");

		if (!STEREOTYPE.equals(peer.getStereotype()))
			throw new IllegalArgumentException("invalid stereotype");

		this.peer = peer;
	}

	@Override
	public Collection<Element> getElements() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		return this.peer.getName();
	}

	@Override
	public void setName(String name) {
		this.peer.setName(name);
	}

	@Override
	public Container getParent() {
		// TODO Auto-generated method stub
		return null;
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
