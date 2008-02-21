package net.lucenews3.http;

import java.util.Enumeration;

public class EmptyEnumeration<E> implements Enumeration<E> {

	@Override
	public boolean hasMoreElements() {
		return false;
	}

	@Override
	public E nextElement() {
		return null;
	}

}
