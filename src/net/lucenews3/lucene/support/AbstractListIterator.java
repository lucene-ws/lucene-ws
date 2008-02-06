package net.lucenews3.lucene.support;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.ListIterator;

public class AbstractListIterator<E> implements ListIterator<E> {

	private Iterator<E> iterator;
	private Link first;
	private Link last;
	private Link current;
	
	public AbstractListIterator(Iterator<E> iterator) {
		this.iterator = iterator;
	}
	
	protected class Link {
		public int index;
		public E value;
		public WeakReference<Link> previous;
		public WeakReference<Link> next;
	}
	
	@Override
	public void add(E e) {
		throw new UnsupportedOperationException();
	}

	protected Link getCurrentLink() {
		return current;
	}
	
	@Override
	public boolean hasNext() {
		if (current == null) {
			if (iterator.hasNext()) {
				E value = iterator.next();
				current = new Link();
				current.index = 0;
				current.value = value;
				current.previous = null;
				current.next = null;
			} else {
				
			}
		} else {
			if (iterator.hasNext()) {
				E value = iterator.next();
				Link next = new Link();
				next.index = current.index + 1;
				next.value = value;
				next.previous = new WeakReference<Link>(current);
				current.next = new WeakReference<Link>(next);
				current = next;
			}
		}
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasPrevious() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public E next() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int nextIndex() {
		return getCurrentLink().index + 1;
	}

	@Override
	public E previous() {
		return getCurrentLink().previous.get().value;
	}

	@Override
	public int previousIndex() {
		return getCurrentLink().index - 1;
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
