package net.lucenews3.http;

import java.util.List;

public interface KeyValueList<K, V> extends List<KeyValue<K, V>> {

	public KeyValueMap<K, V> byKey();
	
}
