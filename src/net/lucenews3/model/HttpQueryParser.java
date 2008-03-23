package net.lucenews3.model;

import javax.servlet.http.HttpServletRequest;

import org.apache.lucene.search.Query;

public class HttpQueryParser implements QueryParser<HttpServletRequest> {

	public static final String DEFAULT_PARAMETER_NAME = "query";
	
	private QueryParser<String> stringParser;
	private String parameterName;
	
	public HttpQueryParser() {
		this.parameterName = DEFAULT_PARAMETER_NAME;
	}
	
	public HttpQueryParser(QueryParser<String> stringParser) {
		this.stringParser = stringParser;
	}
	
	public QueryParser<String> getStringParser() {
		return stringParser;
	}

	public void setStringParser(QueryParser<String> stringParser) {
		this.stringParser = stringParser;
	}

	public String getParameterName() {
		return parameterName;
	}

	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

	@Override
	public Query parse(HttpServletRequest request) {
		final Query result;
		
		final String string = request.getParameter(parameterName);
		if (string == null) {
			result = null;
		} else {
			result = stringParser.parse(string);
		}
		
		return result;
	}

}
