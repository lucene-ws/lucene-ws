package net.lucenews.http;

import java.util.ArrayList;

public class DefaultValueCollection<V> extends ArrayList<V> implements ValueCollection<V> {

	private static final long serialVersionUID = -7351471183123387133L;

	@Override
	public V first() {
		if (isEmpty()) {
			throw new RuntimeException("Cannot obtain the first value in an empty collection");
		} else {
			return get(0);
		}
	}

	@Override
	public V only() {
		int size = size();
		switch (size) {
		case 0:
			throw new RuntimeException("Cannot obtain the only value in an empty collection");
		case 1:
			return get(0);
		default:
			throw new RuntimeException("Cannot obtain the only value in an collection containing " + size + " values");
		}
	}

}
