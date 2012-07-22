package de.proskor.model;

public interface Entity extends Identity {
	/**
	 * Get the package globally unique id.
	 * @return package globally unique id.
	 */
	String getGuid();

	/**
	 * Get the name of the package.
	 * @return name of the package.
	 */
	String getName();

	/**
	 * Set the name of the package.
	 * @param name new name value.
	 */
	void setName(String name);

	/**
	 * Get the description of the package (notes).
	 * @return notes.
	 */
	String getDescription();

	/**
	 * Set the description of the package (notes).
	 * @param description new notes value.
	 */
	void setDescription(String description);

	/**
	 * Get package stereotype.
	 * @return stereotype.
	 */
	String getStereotype();

	/**
	 * Set package stereotype.
	 * @param stereotype new stereotype value.
	 */
	void setStereotype(String stereotype);
}
