package net.lucenews.http;

public class DefaultKeyValue<K, V> implements KeyValue<K, V> {

	private K key;
	private V value;
	
	public DefaultKeyValue(K key, V value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public K getKey() {
		return key;
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
	
}
