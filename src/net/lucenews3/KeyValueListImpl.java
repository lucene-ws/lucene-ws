package net.lucenews3;

import java.util.ArrayList;
import java.util.Collection;

public class KeyValueListImpl<K, V> extends ArrayList<KeyValue<K, V>> implements KeyValueList<K, V> {

	private static final long serialVersionUID = -2879656743297610807L;

	public KeyValueListImpl() {
		super();
	}

	public KeyValueListImpl(Collection<? extends KeyValue<K, V>> collection) {
		super(collection);
	}

	public KeyValueListImpl(int initialCapacity) {
		super(initialCapacity);
	}

	@Override
	public KeyValueMap<K, V> byKey() {
		return new KeyValueMapImpl<K, V>(this);
	}

}
