package de.proskor.model;

public interface Element {
	int getId();
	String getGuid();

	String getName();
	void setName(String name);

	String getDescription();
	void setDescription(String description);

	String getStereotype();
	void setStereotype(String stereotype);

	boolean isChild();

	Package getPackage();
	Element getParent();
	Collection<Element> getElements();
	Collection<Connector> getConnectors();

	Connector connectTo(Element target);
}
