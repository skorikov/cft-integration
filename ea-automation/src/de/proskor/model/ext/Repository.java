package de.proskor.model.ext;

public interface Repository {
	Collection<Package> getModels();
	Package createModel(String name);
}
