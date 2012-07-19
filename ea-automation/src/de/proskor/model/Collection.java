package de.proskor.model;

/**
 * Collection.
 * A simple wrapper for the AI collections.
 * @param <T> type of the contained elements.
 */
public interface Collection<T> extends Iterable<T> {
	/**
	 * Get the size of the collection.
	 * @return size of the collection.
	 */
	public int size();

	/**
	 * Determine if the collection is empty.
	 * @return true if empty, false otherwise.
	 */
	public boolean isEmpty();

	/**
	 * Deletes all elements in the collection.
	 */
	public void clear();

	/**
	 * Check if an element is contained in the collection.
	 * @param element element to check.
	 * @return true if element is contained, false otherwise.
	 */
	public boolean contains(T element);

	/**
	 * Create new element instance.
	 * New elements can only be created using this operation.
	 * @param name element name.
	 * @param type element type, e.g. "Object".
	 * @return new element instance.
	 */
	public T add(String name, String type);

	/**
	 * Remove an element from the collection at given position.
	 * @param index position of the element to remove.
	 */
	public void remove(int index);

	/**
	 * Get an element at given position.
	 * @param index element position.
	 * @return element at given position.
	 */
	public T get(int index);
}
