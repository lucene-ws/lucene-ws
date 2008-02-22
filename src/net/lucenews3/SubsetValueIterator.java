package net.lucenews3;

import java.util.Iterator;

public class SubsetValueIterator<K, V> implements Iterator<V> {
	
	private KeyValueList<K, V> collection;
	private Iterator<KeyValue<K, V>> iterator;
	private Object key;
	private Boolean hasNext;
	private KeyValue<K, V> next;
	private KeyValue<K, V> current;

	public SubsetValueIterator(KeyValueList<K, V> collection, Object key) {
		this.collection = collection;
		this.iterator = this.collection.iterator();
		this.key = key;
		this.hasNext = null;
	}
	
	@Override
	public boolean hasNext() {
		if (hasNext == null) {
			hasNext = false;
			while (iterator.hasNext()) {
				KeyValue<K, V> candidate = iterator.next();
				if (key.equals(candidate.getKey())) {
					next = candidate;
					hasNext = true;
					break;
				}
			}
		}
		return hasNext;
	}

	@Override
	public V next() {
		V result;
		if (hasNext()) {
			current = next;
			result = current.getValue();
			hasNext = null;
		} else {
			throw new RuntimeException("No next!");
		}
		return result;
	}

	@Override
	public void remove() {
		collection.remove(current);
	}

}
