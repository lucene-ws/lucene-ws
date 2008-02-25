package net.lucenews3;

import java.util.List;
import java.util.Map;

public interface KeyValueMap<K, V> extends Map<K, List<V>> {
	
	public void add(K key, V value);
	
	public void put(K key, V value);
	
}
