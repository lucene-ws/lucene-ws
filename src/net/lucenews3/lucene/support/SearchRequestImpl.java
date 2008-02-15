package net.lucenews3.lucene.support;

import java.io.IOException;

import net.lucenews.http.ExceptionWrapper;

import org.apache.lucene.search.Filter;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.Sort;

public class SearchRequestImpl implements SearchRequest {

	private Query query;
	private Filter filter;
	private Sort sort;
	private ExceptionWrapper exceptionWrapper;
	
	public SearchRequestImpl() {
		
	}
	
	public SearchRequestImpl(Query query) {
		this.query = query;
	}
	
	public SearchRequestImpl(Query query, Filter filter) {
		this.query = query;
		this.filter = filter;
	}
	
	public SearchRequestImpl(Query query, Filter filter, Sort sort) {
		this.query = query;
		this.filter = filter;
		this.sort = sort;
	}
	
	public SearchRequestImpl(Query query, Sort sort) {
		this.query = query;
		this.sort = sort;
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
		try {
			return searcher.search(query, filter, sort);
		} catch (IOException e) {
			throw exceptionWrapper.wrap(e);
		}
	}
	
}
