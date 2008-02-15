package net.lucenews3.lucene.support;

import javax.servlet.http.HttpServletRequest;

import org.apache.lucene.search.Sort;

public class HttpSortParser implements SortParser<HttpServletRequest> {

	private SortParser<String> sortParser;
	
	public HttpSortParser() {
		this.sortParser = new SortParserImpl();
	}
	
	@Override
	public Sort parseSort(HttpServletRequest request) {
		Sort result;
		
		final String string = request.getParameter("sort");
		if (string == null) {
			result = null;
		} else {
			result = sortParser.parseSort(string);
		}
		
		return result;
	}

}
