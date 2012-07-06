package de.proskor.model.impl;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import cli.EA.ICollection;
import de.proskor.model.Collection;

/**
 * Basic collection implementation.
 * @param <T> type of elements.
 * @param <E> type of the underlying collection elements.
 */
public abstract class CollectionImpl<T, E> implements Collection<T> {
	private final ICollection peer;
	private int changeCount = 0;
	private int size = -1;

	private class IteratorImpl implements Iterator<T> {
		private int current = 0;
		private int changeCount = CollectionImpl.this.changeCount;

		@Override
		public boolean hasNext() {
			return this.current < CollectionImpl.this.size();
		}

		@Override
		public T next() {
			if (this.changeCount != CollectionImpl.this.changeCount)
				throw new ConcurrentModificationException();

			if (!this.hasNext())
				throw new NoSuchElementException();

			return CollectionImpl.this.get(current++);
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
		
	}

	public CollectionImpl(ICollection peer) {
		this.peer = peer;
	}

	protected abstract boolean matches(E object, T element);

	protected abstract T create(ICollection collection, E element);

	@Override
	public Iterator<T> iterator() {
		return new IteratorImpl();
	}

	@Override
	public int size() {
		this.updateSize();
		return this.size;
	}

	@Override
	public boolean isEmpty() {
		this.updateSize();
		return this.size == 0;
	}

	@Override
	public void clear() {
		this.updateSize();
		if (this.size > 0) {
			for (int i = this.size - 1; i >= 0; i--)
				this.peer.Delete((short) i);
			this.peer.Refresh();
			this.changeCount++;
			this.size = 0;
		}
	}

	@Override
	public boolean contains(T element) {
		this.updateSize();
		for (int i = 0; i < this.size; i++) {
			@SuppressWarnings("unchecked")
			final E object = (E) this.peer.GetAt((short) i);
			if (this.matches(object, element))
				return true;
		}
		return false;
	}

	@Override
	public T add(String name, String type) {
		@SuppressWarnings("unchecked")
		final E object = (E) this.peer.AddNew(name, type);
		final T result = this.create(this.peer, object);
		this.changeCount++;
		this.updateSize();
		this.size++;
		return result;
		// TODO check Refresh and Update protocol
	}

	@Override
	public void remove(int index) {
		if (index < 0 || index >= this.size)
			throw new IndexOutOfBoundsException();

		this.peer.Delete((short) index);
		this.peer.Refresh();
		this.changeCount++;
		this.updateSize();
		this.size--;
	}

	@Override
	public T get(int index) {
		this.updateSize();
		if (index < 0 || index >= this.size)
			throw new IndexOutOfBoundsException();

		@SuppressWarnings("unchecked")
		final E object = (E) this.peer.GetAt((short) index);
		return this.create(this.peer, object);
	}

	private void updateSize() {
		if (this.size < 0)
			this.size = this.peer.get_Count();
	}
}
