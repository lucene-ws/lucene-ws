package net.lucenews3.http;

import java.util.AbstractList;
import java.util.Iterator;

public class ValueListImpl<K, V> extends AbstractList<V> implements ValueList<V> {

	private static final long serialVersionUID = -7351471183123387133L;
	private KeyValueList<K, V> collection;
	private Object key;

	public ValueListImpl(KeyValueList<K, V> collection, Object key) {
		this.collection = collection;
		this.key = key;
	}
	
	@Override
	public V first() {
		if (isEmpty()) {
			throw new RuntimeException("Cannot obtain the first value in an empty collection");
		} else {
			return iterator().next();
		}
	}

	@Override
	public V only() {
		int size = size();
		switch (size) {
		case 0:
			throw new RuntimeException("Cannot obtain the only value in an empty collection");
		case 1:
			return iterator().next();
		default:
			throw new RuntimeException("Cannot obtain the only value in an collection containing " + size + " values");
		}
	}

	@Override
	public int size() {
		int result = 0;
		Iterator<V> iterator = iterator();
		while (iterator.hasNext()) {
			iterator.next();
			result++;
		}
		return result;
	}

	@Override
	public Iterator<V> iterator() {
		return new ValueIteratorImpl<K, V>(collection, key);
	}

	@Override
	public V get(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
