package net.lucenews3.http;

import net.lucenews3.KeyValueList;

public interface HttpUrl {

	public KeyValueList<String, String> getParameters();
	
}
