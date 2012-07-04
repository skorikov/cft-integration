package de.proskor.model;

/**
 * EA Repository.
 */
public interface Repository {

	/**
	 * Allows writing to the output tab.
	 */
	interface OutputTab {
		void write(String text);
		void clear();
		void close();
	}


	/**
	 * Get models.
	 * @return collection of models.
	 */
	public Collection<Package> getModels();

	public OutputTab getOutputTab(String name);
}
