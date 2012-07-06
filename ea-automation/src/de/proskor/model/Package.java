package de.proskor.model;

public interface Package {
	int getId();
	String getGuid();

	String getName();
	void setName(String name);

	String getDescription();
	void setDescription(String description);

	Element getElement();
	Package getParent();
	Collection<Package> getPackages();
	Collection<Diagram> getDiagrams();

	String getStereotype();
	void setStereotype(String stereotype);
}
