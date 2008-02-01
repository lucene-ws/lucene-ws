package net.lucenews.http;

import java.util.ArrayList;

public class DefaultHeaderCollection extends ArrayList<KeyValue<String, String>> implements HeaderCollection {

	private static final long serialVersionUID = -2561665658495368141L;
	
	private KeyValueMap<String, String> byKey;
	
	public DefaultHeaderCollection() {
		this.byKey = new DefaultKeyValueMap<String, String>(this);
	}

	@Override
	public KeyValueMap<String, String> byKey() {
		return byKey;
	}

}
