package net.lucenews3.model;

import javax.servlet.http.HttpServletRequest;

import org.apache.lucene.search.Sort;

public class HttpSortParser implements SortParser<HttpServletRequest> {

	public static final String DEFAULT_PARAMETER_NAME = "sort";
	
	private SortParser<String> stringParser;
	private String parameterName;
	
	public HttpSortParser() {
		this(new SortParserImpl());
	}
	
	public HttpSortParser(SortParser<String> stringParser) {
		this.stringParser = stringParser;
		this.parameterName = DEFAULT_PARAMETER_NAME;
	}
	
	public SortParser<String> getStringParser() {
		return stringParser;
	}

	public void setStringParser(SortParser<String> stringParser) {
		this.stringParser = stringParser;
	}

	public String getParameterName() {
		return parameterName;
	}

	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

	@Override
	public Sort parse(HttpServletRequest request) {
		Sort result;
		
		final String string = request.getParameter(parameterName);
		if (string == null) {
			result = null;
		} else {
			result = stringParser.parse(string);
		}
		
		return result;
	}

}
