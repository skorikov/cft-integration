package de.proskor.model;

/**
 * EA Repository.
 */
public interface Repository extends Writable {
	/**
	 * Get models.
	 * @return collection of models.
	 */
	public Collection<Model> getModels();
}
