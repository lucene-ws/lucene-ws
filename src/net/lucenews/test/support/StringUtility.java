package net.lucenews.test.support;

public class StringUtility {

	private String defaultDelimiter;
	
	public StringUtility() {
		defaultDelimiter = ", ";
	}
	
	public String join(Iterable<?> iterable) {
		return join(defaultDelimiter, iterable);
	}
	
	public String join(String delimiter, Iterable<?> iterable) {
		StringBuffer buffer = new StringBuffer();
		boolean first = true;
		for (Object object : iterable) {
			if (first) {
				first = false;
			} else {
				buffer.append(delimiter);
			}
			buffer.append(object);
		}
		return buffer.toString();
	}
	
	public String padLeft(final Object object, final char padding, final int length) {
		String result = object.toString();
		while (result.length() < length) {
			result = padding + result;
		}
		return result;
	}
	
	public String padRight(final Object object, final char padding, final int length) {
		String result = object.toString();
		while (result.length() < length) {
			result = result + padding;
		}
		return result;
	}
	
}
