package de.proskor.model;

/**
 * Contains packages.
 * May be either a model or a regular package.
 */
public interface Container extends Named, Identity {
	/**
	 * Get contained packages.
	 * @return collection of contained packages.
	 */
	public Collection<Package> getPackages();
}
