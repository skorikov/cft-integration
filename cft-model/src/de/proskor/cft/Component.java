package de.proskor.cft;

import java.util.Collection;

public interface Component extends Container {
	Collection<Event> getEvents();
	Collection<Inport> getInports();
	Collection<Outport> getOutports();
	Collection<Gate> getGates();
	Collection<Component> getComponents();
}
