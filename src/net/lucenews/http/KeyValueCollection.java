package net.lucenews.http;

import java.util.Collection;

public interface KeyValueCollection<K, V> extends Collection<KeyValue<K, V>> {

	public KeyValueMap<K, V> byKey();
	
}
