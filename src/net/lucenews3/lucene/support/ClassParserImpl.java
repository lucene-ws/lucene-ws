package net.lucenews3.lucene.support;

import net.lucenews.http.ExceptionWrapper;

public class ClassParserImpl<T> implements ClassParser<T, String> {

	private Class<T> type;
	private ExceptionWrapper exceptionWrapper;
	
	public ClassParserImpl() {
		this.exceptionWrapper = new DefaultExceptionWrapper();
	}
	
	public ClassParserImpl(Class<T> type) {
		this();
		this.type = type;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Class<T> parse(String string) {
		Class<T> result;
		
		try {
			result = (Class<T>) Class.forName(string);
		} catch (ClassNotFoundException e) {
			throw exceptionWrapper.wrap(e);
		}
		
		// If the base class has been explicitly set, ensure that the
		// parsed class can be assigned to it.
		if (type != null && !type.isAssignableFrom(result)) {
			throw new ClassCastException();
		}
		
		return result;
	}

}
