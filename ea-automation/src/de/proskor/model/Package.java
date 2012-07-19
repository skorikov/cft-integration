package de.proskor.model;

public interface Package {
	int getId();
	String getGuid();

	String getName();
	void setName(String name);

	String getDescription();
	void setDescription(String description);

	String getStereotype();
	void setStereotype(String stereotype);

	boolean isModel();

	Element getElement();
	Package getParent();

	Collection<Package> getPackages();
	Collection<Element> getElements();
	Collection<Diagram> getDiagrams();
}
