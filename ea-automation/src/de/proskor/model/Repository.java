package de.proskor.model;

/**
 * EA Repository.
 */
public interface Repository {
	/**
	 * Allows writing to the output tab.
	 */
	interface OutputTab {
		/**
		 * Outputs text on the output tab.
		 * @param text text.
		 */
		void write(String text);

		/**
		 * Clears the output tab.
		 */
		void clear();

		/**
		 * Removes the output tab.
		 */
		void close();
	}

	/**
	 * Get models.
	 * @return collection of models.
	 */
	Collection<Package> getModels();

	/**
	 * Get an output tab.
	 * @param name name of the output tab.
	 * @return output tab.
	 */
	OutputTab getOutputTab(String name);

	/**
	 * Get package by id.
	 * @param id id of the package.
	 * @return the package with the specified id, or null if none exists.
	 */
	Package getPackageById(int id);

	/**
	 * Get element by id.
	 * @param id id of the element.
	 * @return the element with the specified id, or null if none exists.
	 */
	Element getElementById(int id);

	/**
	 * Get diagram by id.
	 * @param id id of the diagram.
	 * @return the diagram with the specified id, or null if none exists.
	 */
	Diagram getDiagramById(int id);
}
