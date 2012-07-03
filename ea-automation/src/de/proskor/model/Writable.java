package de.proskor.model;

/**
 * Supports write and clear operations.
 */
public interface Writable {
	/**
	 * Write to the output tab.
	 * @param text text that should be displayed.
	 */
	public void write(String text);

	/**
	 * Clear the output tab.
	 */
	public void clear();
}
