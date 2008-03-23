package net.lucenews3.model;

import javax.servlet.http.HttpServletRequest;

import org.apache.lucene.search.Filter;

public class HttpFilterParser implements FilterParser<HttpServletRequest> {

	public static final String DEFAULT_PARAMETER_NAME = "filter";
	
	private FilterParser<String> stringParser;
	private String parameterName;
	
	public HttpFilterParser() {
		this.parameterName = DEFAULT_PARAMETER_NAME;
	}
	
	public HttpFilterParser(FilterParser<String> stringParser) {
		this.stringParser = stringParser;
	}
	
	public FilterParser<String> getStringParser() {
		return stringParser;
	}

	public void setStringParser(FilterParser<String> stringParser) {
		this.stringParser = stringParser;
	}

	public String getParameterName() {
		return parameterName;
	}

	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

	@Override
	public Filter parse(HttpServletRequest request) {
		final Filter result;
		
		String string = request.getParameter(parameterName);
		if (string == null) {
			result = null;
		} else {
			result = stringParser.parse(string);
		}
		
		return result;
	}

}
