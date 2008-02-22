package net.lucenews3;

import java.util.AbstractList;
import java.util.Iterator;

public class SubsetValueList<K, V> extends AbstractList<V> {

	private static final long serialVersionUID = -7351471183123387133L;
	private KeyValueList<K, V> list;
	private K key;

	public SubsetValueList(KeyValueList<K, V> list, K key) {
		this.list = list;
		this.key = key;
	}
	
	@Override
	public boolean add(V value) {
		list.add(new KeyValueImpl<K, V>(key, value));
		return true;
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
		return new SubsetValueIterator<K, V>(list, key);
	}

	@Override
	public V get(int index) {
		// TODO Auto-generated method stub
		Iterator<V> iterator = iterator();
		int currentIndex = -1;
		V value = null;
		while (currentIndex < index) {
			value = iterator.next();
			currentIndex++;
		}
		return value;
	}

}
