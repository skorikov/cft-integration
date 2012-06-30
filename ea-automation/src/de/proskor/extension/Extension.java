package de.proskor.extension;

/**
 * Extension interface. 
 * All extensions should implement this.
 */
public interface Extension extends MenuProvider {
	/**
	 * Called when the extension is started.
	 */
	public void start();

	/**
	 * Called when the extension is stopped.
	 */
	public void stop();
}
