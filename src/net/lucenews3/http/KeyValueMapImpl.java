package net.lucenews3.http;

import java.util.AbstractMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class KeyValueMapImpl<K, V> extends AbstractMap<K, List<V>> implements KeyValueMap<K, V> {

	private KeyValueList<K, V> collection;
	
	public KeyValueMapImpl(KeyValueList<K, V> collection) {
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
	public ValueList<V> get(Object key) {
		return new ValueListImpl<K, V>(collection, key);
	}
	
	@Override
	public ValueList<V> remove(Object key) {
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
	public Set<java.util.Map.Entry<K, List<V>>> entrySet() {
		// TODO Auto-generated method stub
		return null;
	}

}
