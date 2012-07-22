package de.proskor.model;

/**
 * EA Package.
 * If isModel is true, then it is a top-level package (model).
 */
public interface Package extends Entity {
	/**
	 * Determine if the package is a model.
	 * @return true if this is a top-level package, false otherwise.
	 */
	boolean isModel();

	/**
	 * Get the package element.
	 * @return package element.
	 */
	Element getElement();

	/**
	 * Get the package parent.
	 * Throws IllegalStateException if called on a model.
	 * @return package parent.
	 */
	Package getParent();

	/**
	 * Get sub-packages.
	 * @return sub-packages.
	 */
	Collection<Package> getPackages();

	/**
	 * Get contained elements.
	 * @return elements.
	 */
	Collection<Element> getElements();

	/**
	 * Get contained diagrams.
	 * @return diagrams.
	 */
	Collection<Diagram> getDiagrams();
}
