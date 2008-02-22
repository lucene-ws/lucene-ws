package net.lucenews3.http;

import java.util.ArrayList;

public class ParameterCollectionImpl extends ArrayList<KeyValue<String, String>> implements ParameterCollection {

	private static final long serialVersionUID = -8936358817363622985L;
	
	private KeyValueMap<String, String> byKey;
	
	public ParameterCollectionImpl() {
		this.byKey = new KeyValueMapImpl<String, String>(this);
	}

	@Override
	public KeyValueMap<String, String> byKey() {
		return byKey;
	}

}
