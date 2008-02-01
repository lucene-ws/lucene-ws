package net.lucenews.http;

import java.util.ArrayList;

public class DefaultParameterCollection extends ArrayList<KeyValue<String, String>> implements ParameterCollection {

	private static final long serialVersionUID = -8936358817363622985L;
	
	private KeyValueMap<String, String> byKey;
	
	public DefaultParameterCollection() {
		this.byKey = new DefaultKeyValueMap<String, String>(this);
	}

	@Override
	public KeyValueMap<String, String> byKey() {
		return byKey;
	}

}
