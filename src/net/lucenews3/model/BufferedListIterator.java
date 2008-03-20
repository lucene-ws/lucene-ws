package net.lucenews3.model;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * A list iterator built upon a standard iterator. Caches items as they
 * are traversed in order to support backwards iteration.
 *
 * @param <E>
 */
public class BufferedListIterator<E> implements ListIterator<E> {

	private final Iterator<E> iterator;
	private Link currentLink;
	
	public BufferedListIterator(Iterator<E> iterator) {
		this.iterator = iterator;
	}
	
	protected class Link {
		public int index;
		public E value;
		public Link previous;
		public Link next;
		
		public Link(int index, E value) {
			this.index = index;
			this.value = value;
		}
	}
	
	@Override
	public void add(E e) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Reads the next value if necessary to determine whether
	 * or not a next value exists.
	 */
	@Override
	public boolean hasNext() {
		boolean result;
		
		if (currentLink == null || currentLink.next == null) {
			result = iterator.hasNext();
		} else {
			result = true;
		}
		
		return result;
	}

	@Override
	public boolean hasPrevious() {
		boolean result;
		
		if (currentLink == null) {
			result = false;
		} else {
			result = (currentLink.previous != null);
		}
		
		return result;
	}

	@Override
	public E next() {
		E result;
		
		if (!hasNext()) {
			throw new NoSuchElementException();
		}
		
		if (currentLink == null) {
			E nextValue = iterator.next();
			currentLink = new Link(0, nextValue);
			result = nextValue;
		} else if (currentLink.next == null) {
			// The "next" value has not yet been read
			int nextIndex = currentLink.index + 1;
			E nextValue = iterator.next();
			Link nextLink = new Link(nextIndex, nextValue);
			
			currentLink.next = nextLink;
			nextLink.previous = currentLink;
			
			result = nextValue;
			
			// Move the current link ahead
			currentLink = nextLink;
		} else {
			// The "next" value has already been read
			Link nextLink = currentLink.next;
			result = nextLink.value;
			currentLink = nextLink;
		}
		
		return result;
	}

	@Override
	public int nextIndex() {
		int result;
		
		if (currentLink == null) {
			result = 0;
		} else {
			result = currentLink.index + 1;
		}
		
		return result;
	}

	@Override
	public E previous() {
		E result;
		
		if (hasPrevious()) {
			Link previousLink = currentLink.previous;
			result = previousLink.value;
			currentLink = previousLink;
		} else {
			throw new NoSuchElementException();
		}
		
		
		return result;
	}

	@Override
	public int previousIndex() {
		int result;
		
		if (hasPrevious()) {
			result = currentLink.previous.index;
		} else {
			throw new RuntimeException();
		}
		
		return result;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void set(E e) {
		throw new UnsupportedOperationException();
	}

}
