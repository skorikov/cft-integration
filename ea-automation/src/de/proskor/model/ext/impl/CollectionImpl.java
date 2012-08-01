package de.proskor.model.ext.impl;

import java.util.Iterator;

public abstract class CollectionImpl<T, P> implements de.proskor.model.ext.Collection<T> {
	private final de.proskor.model.Collection<P> peer;

	public CollectionImpl(de.proskor.model.Collection<P> peer) {
		this.peer = peer;
	}

	abstract T wrap(P element);
	abstract P unwrap(T element);

	private class IteratorImpl implements Iterator<T> {
		private final Iterator<P> peer;

		private IteratorImpl(Iterator<P> peer) {
			this.peer = peer;
		}

		@Override
		public boolean hasNext() {
			return this.peer.hasNext();
		}

		@Override
		public T next() {
			return wrap(this.peer.next());
		}

		@Override
		public void remove() {
			this.peer.remove();
		}
	}

	@Override
	public Iterator<T> iterator() {
		return new IteratorImpl(peer.iterator());
	}

	@Override
	public int size() {
		return this.peer.size();
	}

	@Override
	public boolean isEmpty() {
		return this.peer.isEmpty();
	}

	@Override
	public void clear() {
		this.peer.clear();
	}

	@Override
	public boolean contains(T element) {
		return this.peer.contains(unwrap(element));
	}

	@Override
	public void remove(T element) {
		this.peer.remove(unwrap(element));
	}
}
