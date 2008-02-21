package net.lucenews3.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

public abstract class AbstractIteratorList<E> implements List<E> {

	@Override
	public boolean add(E e) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void add(int index, E element) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean contains(Object o) {
		boolean result = false;
		for (Iterator<E> i = iterator(); i.hasNext();) {
			E element = i.next();
			if (o.equals(element)) {
				result = true;
				break;
			}
		}
		return result;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		Set<Object> values = new HashSet<Object>();
		values.addAll(c);
		
		for (Iterator<E> i = iterator(); i.hasNext();) {
			E element = i.next();
			values.remove(element);
			if (values.isEmpty()) {
				break;
			}
		}
		
		return values.isEmpty();
	}

	@Override
	public E get(int index) {
		E result = null;
		boolean hasResult = false;
		
		int currentIndex = 0;
		for (Iterator<E> i = iterator(); i.hasNext(); currentIndex++) {
			E element = i.next();
			if (currentIndex == index) {
				result = element;
				hasResult = true;
				break;
			}
		}
		
		if (!hasResult) {
			throw new IndexOutOfBoundsException();
		}
		
		return result;
	}

	@Override
	public int indexOf(Object o) {
		int result = -1;
		int index = 0;
		for (Iterator<E> i = iterator(); i.hasNext(); index++) {
			E element = i.next();
			if (o.equals(element)) {
				result = index;
				break;
			}
		}
		return result;
	}

	@Override
	public boolean isEmpty() {
		return !iterator().hasNext();
	}

	@Override
	public int lastIndexOf(Object o) {
		int result = -1;
		int index = 0;
		for (Iterator<E> i = iterator(); i.hasNext(); index++) {
			E element = i.next();
			if (o.equals(element)) {
				result = index;
			}
		}
		return result;
	}

	@Override
	public ListIterator<E> listIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean remove(Object o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public E remove(int index) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public E set(int index, E element) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int size() {
		int result = 0;
		for (Iterator<E> i = iterator(); i.hasNext();) {
			result++;
		}
		return result;
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object[] toArray() {
		List<Object> result = new ArrayList<Object>();
		for (Iterator<E> i = iterator(); i.hasNext();) {
			result.add(i.next());
		}
		return result.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		// TODO Auto-generated method stub
		return null;
	}

}
