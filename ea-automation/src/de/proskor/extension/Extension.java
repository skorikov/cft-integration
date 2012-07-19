package de.proskor.extension;

import de.proskor.model.Repository;

/**
 * Extension interface. 
 * All extensions should implement this.
 */
public interface Extension extends MenuProvider, DeletionListener {
	/**
	 * Called when the extension is started.
	 */
	public void start();

	/**
	 * Called when the repository is initialized.
	 * Clients should store the reference to the repository.
	 * @param repository repository.
	 */
	public void initialize(Repository repository);

	/**
	 * Called when the extension is stopped.
	 */
	public void stop();
}
