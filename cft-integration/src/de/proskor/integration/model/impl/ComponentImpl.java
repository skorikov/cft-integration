package de.proskor.integration.model.impl;

import java.util.ArrayList;
import java.util.Collection;

import de.proskor.integration.model.BasicEvent;
import de.proskor.integration.model.Component;
import de.proskor.integration.model.Connection;
import de.proskor.integration.model.Inport;
import de.proskor.integration.model.Outport;

public class ComponentImpl implements Component {
	private String name;
	private Collection<BasicEvent> events = new ArrayList<BasicEvent>();

	public ComponentImpl() {}

	public ComponentImpl(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Component find(Component kid) {
		// TODO
		return kid;
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
	public Collection<BasicEvent> getBasicEvents() {
		return this.events;
	}

	@Override
	public Collection<Component> getComponents() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Connection> getConnections() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
