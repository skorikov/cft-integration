package de.proskor.model.ext;

public interface Collection<T> extends Iterable<T> {
	int size();
	boolean isEmpty();
	void clear();
	boolean contains(T element);
	void remove(T element);
}
