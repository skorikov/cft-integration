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
abstract class CollectionImpl<T, E> implements Collection<T> {
	private final ICollection peer;
	private int changeCount = 0;

	private class IteratorImpl implements Iterator<T> {
		private int current = -1;
		private int changeCount = CollectionImpl.this.changeCount;
		private boolean deleted = false;

		@Override
		public boolean hasNext() {
			return (this.current + 1) < CollectionImpl.this.size();
		}

		@Override
		public T next() {
			if (this.changeCount != CollectionImpl.this.changeCount)
				throw new ConcurrentModificationException();

			if (!this.hasNext())
				throw new NoSuchElementException();

			this.current++;
			this.deleted = false;

			return CollectionImpl.this.get(current);
		}

		@Override
		public void remove() {
			if ((this.current < 0) || this.deleted)
				throw new IllegalStateException();

			CollectionImpl.this.removeAt(current);
			this.changeCount++;
			this.current--;
			this.deleted = true;
		}
		
	}

	public CollectionImpl(ICollection peer) {
		if (peer == null)
			throw new NullPointerException();

		this.peer = peer;
	}

	protected abstract boolean matches(E object, T element);

	protected abstract T create(E element);

	@Override
	public Iterator<T> iterator() {
		return new IteratorImpl();
	}

	@Override
	public int size() {
		this.peer.Refresh();
		return this.peer.get_Count();
	}

	@Override
	public boolean isEmpty() {
		return this.size() == 0;
	}

	@Override
	public void clear() {
		final int size = this.size();
		if (size > 0) {
			for (int i = size - 1; i >= 0; i--)
				this.peer.Delete((short) i);
			this.changeCount++;
		}
	}

	@Override
	public boolean contains(T element) {
		if (element == null)
			throw new NullPointerException();

		final int size = this.size();
		for (int i = 0; i < size; i++) {
			@SuppressWarnings("unchecked")
			final E object = (E) this.peer.GetAt((short) i);
			if (this.matches(object, element))
				return true;
		}
		return false;
	}

	@Override
	public T add(String name, String type) {
		if ((name == null) || (type == null))
			throw new NullPointerException();

		this.peer.Refresh();
		@SuppressWarnings("unchecked")
		final E object = (E) this.peer.AddNew(name, type);
		final T result = this.create(object);
		this.changeCount++;
		return result;
	}

	@Override
	public void removeAt(int index) {
		if (index < 0 || index >= this.size())
			throw new IndexOutOfBoundsException();

		this.peer.Delete((short) index);
		this.changeCount++;
	}

	@Override
	public void remove(T element) {
		if (element == null)
			throw new NullPointerException();

		final int size = this.size();
		for (int i = 0; i < size; i++) {
			@SuppressWarnings("unchecked")
			final E object = (E) this.peer.GetAt((short) i);
			if (this.matches(object, element)) {
				this.peer.Delete((short) i);
				return;
			}
		}
		throw new NoSuchElementException();
	}

	@Override
	public T get(int index) {
		if (index < 0 || index >= this.size())
			throw new IndexOutOfBoundsException();

		@SuppressWarnings("unchecked")
		final E object = (E) this.peer.GetAt((short) index);
		return this.create(object);
	}

	@Override
	public int indexOf(T element) {
		if (element == null)
			throw new NullPointerException();

		final int size = this.size();
		for (int i = 0; i < size; i++) {
			@SuppressWarnings("unchecked")
			final E object = (E) this.peer.GetAt((short) i);
			if (this.matches(object, element))
				return i;
		}
		throw new NoSuchElementException();
	}

	@Override
	public boolean equals(Object that) {
		if (that == null)
			return false;

		if (that instanceof CollectionImpl) {
			final CollectionImpl<?, ?> collection = (CollectionImpl<?, ?>) that;
			final int size = this.size();

			if (size != collection.size())
				return false;

			for (int i = 0; i < size; i++) {
				if (!this.get(i).equals(collection.get(i)))
					return false;
			}

			return true;
		}

		return false;
	}

	@Override
	public int hashCode() {
		final int size = this.size();
		int hash = 0;
		
		for (int i = 0; i < size; i++) {
			hash = (hash * 7) + this.get(i).hashCode();
		}

		return hash;
	}
}
