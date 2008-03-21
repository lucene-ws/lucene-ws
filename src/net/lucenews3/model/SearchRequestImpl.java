package net.lucenews3.model;

import java.io.IOException;

import net.lucenews3.ExceptionTranslator;

import org.apache.log4j.Logger;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.Sort;

public class SearchRequestImpl implements SearchRequest {

	private Query query;
	private Filter filter;
	private Sort sort;
	private ExceptionTranslator exceptionTranslator;
	private Logger logger;
	
	public SearchRequestImpl() {
		this.logger = Logger.getLogger(getClass());
	}
	
	public SearchRequestImpl(Query query) {
		this.query = query;
		this.logger = Logger.getLogger(getClass());
	}
	
	public SearchRequestImpl(Query query, Filter filter) {
		this.query = query;
		this.filter = filter;
		this.logger = Logger.getLogger(getClass());
	}
	
	public SearchRequestImpl(Query query, Filter filter, Sort sort) {
		this.query = query;
		this.filter = filter;
		this.sort = sort;
		this.logger = Logger.getLogger(getClass());
	}
	
	public SearchRequestImpl(Query query, Sort sort) {
		this.query = query;
		this.sort = sort;
		this.logger = Logger.getLogger(getClass());
	}

	public Query getQuery() {
		return query;
	}

	public void setQuery(Query query) {
		this.query = query;
	}

	public Filter getFilter() {
		return filter;
	}

	public void setFilter(Filter filter) {
		this.filter = filter;
	}

	public Sort getSort() {
		return sort;
	}

	public void setSort(Sort sort) {
		this.sort = sort;
	}

	@Override
	public Hits search(Searcher searcher) {
		Hits results;
		
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("Searching with query \"" + query + "\", filter \"" + filter + "\" and sort \"" + sort + "\"");
			}
			results = searcher.search(query, filter, sort);
			if (logger.isDebugEnabled()) {
				logger.debug("Search returns " + results.length() + " hits");
			}
		} catch (IOException e) {
			throw exceptionTranslator.translate(e);
		}
		
		return results;
	}
	
}
