package de.proskor.integration.model;

import java.util.Set;


public interface Component {
	String getName();

	Component find(Component kid);

	Set<Inport> getInports();
	Set<Outport> getOutports();
	Set<BasicEvent> getBasicEvents();

	Set<Component> getComponents();
	Set<Connection> getConnections();
}
