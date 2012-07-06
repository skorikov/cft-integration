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
	public Collection<Package> getModels();

	/**
	 * Get an output tab.
	 * @param name name of the output tab.
	 * @return output tab.
	 */
	public OutputTab getOutputTab(String name);
}
