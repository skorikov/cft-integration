package de.proskor.integration.model.impl;

import java.util.Set;

import de.proskor.integration.model.BasicEvent;
import de.proskor.integration.model.Component;
import de.proskor.integration.model.Connection;
import de.proskor.integration.model.Inport;
import de.proskor.integration.model.Outport;

public class ComponentImpl implements Component {
	@Override
	public String getName() {
		// TODO
		return "NAME";
	}

	@Override
	public Component find(Component kid) {
		// TODO
		return kid;
	}

	@Override
	public Set<Inport> getInports() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Outport> getOutports() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<BasicEvent> getBasicEvents() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Component> getComponents() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Connection> getConnections() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
