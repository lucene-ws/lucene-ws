package net.lucenews3.lucene.support;

import javax.servlet.http.HttpServletRequest;

import org.apache.lucene.search.Query;

public class HttpQueryParser implements QueryParser<HttpServletRequest> {

	private QueryParser<String> queryParser;
	
	public HttpQueryParser(QueryParser<String> queryParser) {
		this.queryParser = queryParser;
	}
	
	@Override
	public Query parseQuery(HttpServletRequest request) {
		Query result;
		
		String string = request.getParameter("query");
		if (string == null) {
			result = null;
		} else {
			result = queryParser.parseQuery(string);
		}
		
		return result;
	}

}
