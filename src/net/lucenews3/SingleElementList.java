package net.lucenews3;

import java.util.AbstractList;

public class SingleElementList<E> extends AbstractList<E> {

	public E element;

	public SingleElementList() {
		this(null);
	}

	public SingleElementList(E element) {
		this.element = element;
	}

	@Override
	public E get(int index) {
		if (index == 0) {
			return element;
		} else {
			throw new IndexOutOfBoundsException();
		}
	}

	@Override
	public E set(int index, E element) {
		if (index == 0) {
			E previous = this.element;
			this.element = element;
			return previous;
		} else {
			throw new IndexOutOfBoundsException();
		}
	}

	@Override
	public int size() {
		return 1;
	}

}
