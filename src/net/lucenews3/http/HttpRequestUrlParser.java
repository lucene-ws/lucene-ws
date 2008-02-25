package net.lucenews3.http;

import java.net.MalformedURLException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.lucenews3.ExceptionTranslator;
import net.lucenews3.ExceptionTranslatorImpl;

public class HttpRequestUrlParser implements UrlParser<HttpServletRequest> {

	private ExceptionTranslator exceptionTranslator;
	
	public HttpRequestUrlParser() {
		this(new ExceptionTranslatorImpl());
	}
	
	public HttpRequestUrlParser(ExceptionTranslator exceptionTranslator) {
		this.exceptionTranslator = exceptionTranslator;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Url parse(HttpServletRequest request) {
		Url result;
		
		try {
			result = new UrlImpl(request.getRequestURL().toString());
		} catch (MalformedURLException e) {
			throw exceptionTranslator.translate(e);
		}
		
		Map<String, String[]> parameters = (Map<String, String[]>) request.getParameterMap();
		for (String name : parameters.keySet()) {
			String[] values = parameters.get(name);
			for (String value : values) {
				result.getParameters().byKey().add(name, value);
			}
		}
		
		return result;
	}

}
