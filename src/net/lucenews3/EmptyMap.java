package net.lucenews3;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class EmptyMap<K, V> implements Map<K, V> {

	@Override
	public void clear() {
		
	}

	@Override
	public boolean containsKey(Object key) {
		return false;
	}

	@Override
	public boolean containsValue(Object value) {
		return false;
	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		return Collections.emptySet();
	}

	@Override
	public V get(Object key) {
		return null;
	}

	@Override
	public boolean isEmpty() {
		return true;
	}

	@Override
	public Set<K> keySet() {
		return Collections.emptySet();
	}

	@Override
	public V put(K key, V value) {
		return null;
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		
	}

	@Override
	public V remove(Object key) {
		return null;
	}

	@Override
	public int size() {
		return 0;
	}

	@Override
	public Collection<V> values() {
		return Collections.emptySet();
	}

}
