package de.proskor.model;

public interface Collection<T extends Identity> extends Iterable<T> {
	public int size();
	public boolean isEmpty();
	public void clear();
	public boolean contains(T element);
	public T add(String name, String type);
	public void remove(int index);
	public T get(int index);
}
