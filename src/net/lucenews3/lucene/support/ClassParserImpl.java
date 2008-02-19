package net.lucenews3.lucene.support;

import net.lucenews3.ExceptionTranslator;
import net.lucenews3.ExceptionTranslatorImpl;

public class ClassParserImpl<T> implements ClassParser<T, String> {

	private Class<T> assignableType;
	private ExceptionTranslator exceptionTranslator;
	
	public ClassParserImpl() {
		this.exceptionTranslator = new ExceptionTranslatorImpl();
	}
	
	public ClassParserImpl(Class<T> assignableType) {
		this();
		this.assignableType = assignableType;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Class<T> parse(String string) {
		Class<T> result;
		
		try {
			result = (Class<T>) Class.forName(string);
		} catch (ClassNotFoundException e) {
			throw exceptionTranslator.translate(e);
		}
		
		// If the base class has been explicitly set, ensure that the
		// parsed class can be assigned to it.
		if (assignableType != null && !assignableType.isAssignableFrom(result)) {
			throw new ClassCastException();
		}
		
		return result;
	}

}
