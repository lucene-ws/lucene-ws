package net.lucenews3.lucene.support;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.ListIterator;

/**
 * A list iterator built upon a standard iterator. Caches items as they
 * are traversed in order to support backwards iteration.
 *
 * @param <E>
 */
public class BufferedListIterator<E> implements ListIterator<E> {

	private Iterator<E> iterator;
	private Link currentLink;
	
	public BufferedListIterator(Iterator<E> iterator) {
		this.iterator = iterator;
	}
	
	protected class Link {
		public int index;
		public E value;
		public WeakReference<Link> previous;
		public WeakReference<Link> next;
		
		public Link() {
			
		}
		
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
			throw new RuntimeException();
		}
		
		if (currentLink == null) {
			E nextValue = iterator.next();
			currentLink = new Link(0, nextValue);
			result = nextValue;
		} else {
			if (currentLink.next == null) {
				// The "next" value has not yet been read
				int nextIndex = currentLink.index + 1;
				E nextValue = iterator.next();
				Link nextLink = new Link(nextIndex, nextValue);
				
				currentLink.next = new WeakReference<Link>(nextLink);
				nextLink.previous = new WeakReference<Link>(currentLink);
				
				result = nextValue;
				
				// Move the current link ahead
				currentLink = nextLink;
			} else {
				// The "next" value has already been read
				Link nextLink = currentLink.next.get();
				result = nextLink.value;
				currentLink = nextLink;
			}
		}
		
		return result;
	}

	@Override
	public int nextIndex() {
		int result;
		
		if (!hasNext()) {
			throw new RuntimeException();
		}
		
		Link nextLink = currentLink.next.get();
		result = nextLink.index;
		
		return result;
	}

	@Override
	public E previous() {
		E result;
		
		if (!hasPrevious()) {
			throw new RuntimeException();
		}
		
		Link previousLink = currentLink.previous.get();
		result = previousLink.value;
		currentLink = previousLink;
		
		return result;
	}

	@Override
	public int previousIndex() {
		int result;
		
		if (!hasPrevious()) {
			throw new RuntimeException();
		}
		
		Link previousLink = currentLink.previous.get();
		result = previousLink.index;
		
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
