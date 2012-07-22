package de.proskor.model;

public interface Element extends Entity {
	boolean isChild();

	Package getPackage();
	Element getParent();
	Collection<Element> getElements();
	Collection<Connector> getConnectors();

	Connector connectTo(Element target);
}
