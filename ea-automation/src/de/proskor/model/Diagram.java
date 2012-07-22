package de.proskor.model;

public interface Diagram extends Entity {
	Package getPackage();
	Collection<Node> getNodes();
}
