package net.lucenews3.http;

import java.util.AbstractMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class KeyValueMapImpl<K, V> extends AbstractMap<K, ValueCollection<V>> implements KeyValueMap<K, V> {

	private KeyValueCollection<K, V> collection;
	
	public KeyValueMapImpl(KeyValueCollection<K, V> collection) {
		this.collection = collection;
	}
	
	@Override
	public boolean containsKey(Object key) {
		return keySet().contains(key);
	}
	
	@Override
	public int size() {
		return keySet().size();
	}
	
	@Override
	public Set<K> keySet() {
		Set<K> result = new HashSet<K>();
		for (KeyValue<K, V> pair : collection) {
			result.add(pair.getKey());
		}
		return result;
	}
	
	@Override
	public ValueCollection<V> get(Object key) {
		return new ValueCollectionImpl<K, V>(collection, key);
	}
	
	@Override
	public ValueCollection<V> remove(Object key) {
		Iterator<KeyValue<K, V>> i = collection.iterator();
		while (i.hasNext()) {
			KeyValue<K, V> pair = i.next();
			if (key.equals(pair.getKey())) {
				i.remove();
			}
		}
		return null;
	}
	
	@Override
	public Set<java.util.Map.Entry<K, ValueCollection<V>>> entrySet() {
		// TODO Auto-generated method stub
		return null;
	}

}
