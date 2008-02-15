package net.lucenews3.lucene.support;

import javax.servlet.http.HttpServletRequest;

import org.apache.lucene.search.Filter;

public class HttpFilterParser implements FilterParser<HttpServletRequest> {

	private FilterParser<String> filterParser;
	
	public HttpFilterParser() {
		
	}
	
	@Override
	public Filter parseFilter(HttpServletRequest request) {
		Filter result;
		
		String string = request.getParameter("filter");
		if (string == null) {
			result = null;
		} else {
			result = filterParser.parseFilter(string);
		}
		
		return result;
	}

}
