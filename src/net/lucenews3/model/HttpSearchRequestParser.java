package net.lucenews3.model;

import javax.servlet.http.HttpServletRequest;

import org.apache.lucene.search.Filter;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;

public class HttpSearchRequestParser implements SearchRequestParser<HttpServletRequest> {

	private QueryParser<HttpServletRequest> queryParser;
	private FilterParser<HttpServletRequest> filterParser;
	private SortParser<HttpServletRequest> sortParser;
	
	public QueryParser<HttpServletRequest> getQueryParser() {
		return queryParser;
	}

	public void setQueryParser(QueryParser<HttpServletRequest> queryParser) {
		this.queryParser = queryParser;
	}

	public FilterParser<HttpServletRequest> getFilterParser() {
		return filterParser;
	}

	public void setFilterParser(FilterParser<HttpServletRequest> filterParser) {
		this.filterParser = filterParser;
	}

	public SortParser<HttpServletRequest> getSortParser() {
		return sortParser;
	}

	public void setSortParser(SortParser<HttpServletRequest> sortParser) {
		this.sortParser = sortParser;
	}

	@Override
	public SearchRequest parse(HttpServletRequest request) {
		final Query query = queryParser.parse(request);
		final Filter filter = filterParser.parse(request);
		final Sort sort = sortParser.parse(request);
		return new SearchRequestImpl(query, filter, sort);
	}

}
