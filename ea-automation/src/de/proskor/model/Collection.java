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
	int size();

	/**
	 * Determine if the collection is empty.
	 * @return true if empty, false otherwise.
	 */
	boolean isEmpty();

	/**
	 * Deletes all elements in the collection.
	 */
	void clear();

	/**
	 * Check if an element is contained in the collection.
	 * @param element element to check.
	 * @return true if element is contained, false otherwise.
	 */
	boolean contains(T element);

	/**
	 * Create new element instance.
	 * New elements can only be created using this operation.
	 * @param name element name.
	 * @param type element type, e.g. "Object".
	 * @return new element instance.
	 */
	T add(String name, String type);

	/**
	 * Remove an element from the collection at given position.
	 * Throws IndexOutOfBoundsException if index is invalid.
	 * @param index position of the element to remove.
	 */
	void removeAt(int index);

	/**
	 * Remove an element from the collection.
	 * Throws NoSuchElementException if the element is not contained in the collection.
	 * @param element element to remove.
	 */
	void remove(T element);

	/**
	 * Get an element at given position.
	 * @param index element position.
	 * @return element at given position.
	 */
	T get(int index);

	/**
	 * Get the index of the contained element.
	 * @param element element.
	 * @return index of the element in the collection.
	 */
	int indexOf(T element);
}
