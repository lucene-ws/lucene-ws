package net.lucenews3.http;

import java.util.ArrayList;

public class HeaderCollectionImpl extends ArrayList<KeyValue<String, String>> implements HeaderCollection {

	private static final long serialVersionUID = -2561665658495368141L;
	
	private KeyValueMap<String, String> byKey;
	
	public HeaderCollectionImpl() {
		this.byKey = new KeyValueMapImpl<String, String>(this);
	}

	@Override
	public KeyValueMap<String, String> byKey() {
		return byKey;
	}

}
