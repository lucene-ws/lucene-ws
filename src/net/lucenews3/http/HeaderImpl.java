package net.lucenews3.http;

public class HeaderImpl implements Header {

	private String key;
	private String value;
	
	public HeaderImpl(String key, String value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public String getKey() {
		return key;
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public String setValue(String value) {
		String result = this.value;
		this.value = value;
		return result;
	}
	
	@Override
	public String toString() {
		return "header(" + key + ": " + value + ")";
	}

}