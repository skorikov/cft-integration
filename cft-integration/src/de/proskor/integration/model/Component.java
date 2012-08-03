package de.proskor.integration.model;

import java.util.Collection;

public interface Component {
	String getName();
	void setName(String name);

	Component find(Component component);

	Collection<Inport> getInports();
	Collection<Outport> getOutports();
	Collection<BasicEvent> getBasicEvents();

	Collection<Component> getComponents();
	Collection<Connection> getConnections();
}
