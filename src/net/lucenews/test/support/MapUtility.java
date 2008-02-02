package net.lucenews.test.support;

import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;

public class MapUtility {
	
	public <K, V> Map<K, V> toTypedMap(Class<K> keyType, Class<V> valueType, Object... objects) {
		
		return null;
	}
	
	/**
	 * Generic map creation. Experimental.
	 * @param <K> key type
	 * @param <V> value type
	 * @param <T> map type
	 * @param type
	 * @return
	 */
	public <K, V, T extends Map<K, V>> T to(final Class<T> mapType, final Class<K> keyType, final Class<V> valueType) {
		T result;
		try {
			result = mapType.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return result;
	}
	
	public Map<?, ?> toMap(final Object... objects) {
		final Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		populate(result, objects);
		return result;
	}
	
	public Hashtable<?, ?> toHashtable(final Object... objects) {
		Hashtable<Object, Object> result = new Hashtable<Object, Object>();
		populate(result, objects);
		return result;
	}
	
	public void populate(final Map<Object, Object> map, final Object... objects) {
		for (int i = 0; i < objects.length; i += 2) {
			Object key = objects[i];
			Object value;
			try {
				value = objects[i + 1];
			} catch (ArrayIndexOutOfBoundsException badIndex) {
				value = null;
			}
			map.put(key, value);
		}
	}

}
