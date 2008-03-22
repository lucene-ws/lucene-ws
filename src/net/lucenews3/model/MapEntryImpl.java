package net.lucenews3.model;

import java.util.Map;

public class MapEntryImpl<K, V> implements Map.Entry<K, V> {

	private K key;
	private V value;
	
	public MapEntryImpl() {
		
	}
	
	public MapEntryImpl(K key, V value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public K getKey() {
		return key;
	}
	
	public K setKey(K key) {
		K result = this.key;
		this.key = key;
		return result;
	}

	@Override
	public V getValue() {
		return value;
	}

	@Override
	public V setValue(V value) {
		V result = this.value;
		this.value = value;
		return result;
	}
	
	@Override
	public int hashCode() {
		return (key.hashCode() << 16) | value.hashCode();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object other) {
		final boolean result;
		
		if (other instanceof Map.Entry) {
			Map.Entry<K, V> otherEntry = (Map.Entry<K, V>) other;
			result = key.equals(otherEntry.getKey()) && value.equals(otherEntry.getValue());
		} else {
			result = false;
		}
		
		return result;
	}
	
	@Override
	public String toString() {
		return key + "=" + value;
	}

}
