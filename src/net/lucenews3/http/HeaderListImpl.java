package net.lucenews3.http;

import java.util.ArrayList;

import net.lucenews3.KeyValue;
import net.lucenews3.KeyValueMap;
import net.lucenews3.KeyValueMapImpl;

public class HeaderListImpl extends ArrayList<KeyValue<String, String>> implements HeaderList {

	private static final long serialVersionUID = -2561665658495368141L;
	
	private KeyValueMap<String, String> byKey;
	
	public HeaderListImpl() {
		this.byKey = new KeyValueMapImpl<String, String>(this);
	}

	@Override
	public KeyValueMap<String, String> byKey() {
		return byKey;
	}

}
