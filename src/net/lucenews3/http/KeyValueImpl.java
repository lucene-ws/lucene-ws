package net.lucenews3.http;

public class KeyValueImpl<K, V> implements KeyValue<K, V> {

	private K key;
	private V value;
	
	public KeyValueImpl() {
		
	}
	
	public KeyValueImpl(K key, V value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public K getKey() {
		return key;
	}
	
	public void setKey(K key) {
		this.key = key;
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
