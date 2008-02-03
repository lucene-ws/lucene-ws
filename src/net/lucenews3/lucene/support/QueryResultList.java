package net.lucenews3.lucene.support;

import java.io.IOException;
import java.util.AbstractList;

import net.lucenews.http.ExceptionWrapper;

import org.apache.lucene.misc.ChainedFilter;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.Sort;

public class QueryResultList extends AbstractList<Result> implements ResultList {

	private ExceptionWrapper exceptionWrapper;
	private Searcher searcher;
	private Query query;
	private Filter filter;
	private Sort sort;
	private Hits hits;
	private boolean initialized;
	
	public QueryResultList(QueryResultList prototype) {
		this.exceptionWrapper = prototype.exceptionWrapper;
		this.searcher = prototype.searcher;
		this.query = prototype.query;
		this.filter = prototype.filter;
		this.sort = prototype.sort;
		this.hits = prototype.hits;
		this.initialized = prototype.initialized;
	}
	
	public QueryResultList(Searcher searcher) {
		this(searcher, new MatchAllDocsQuery());
	}
	
	public QueryResultList(Searcher searcher, Query query) {
		this.searcher = searcher;
		this.query = query;
	}
	
	public void initialize() {
		if (!initialized) {
			try {
				hits = searcher.search(query);
			} catch (IOException e) {
				throw exceptionWrapper.wrap(e);
			}
			initialized = true;
		}
	}
	
	@Override
	public Result get(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int size() {
		return hits.length();
	}

	@Override
	public QueryResultList filteredBy(Filter filter) {
		final QueryResultList result = new QueryResultList(this);
		if (filter == null) {
			result.filter = null;
		} else if (result.filter == null) {
			result.filter = filter;
		} else {
			result.filter = new ChainedFilter(new Filter[]{ result.filter, filter }, ChainedFilter.AND);
		}
		result.initialized = false;
		return result;
	}
	
	public Filter merge(Filter base, Filter delta) {
		return null;
	}

	@Override
	public QueryResultList sortedBy(Sort sort) {
		final QueryResultList result = new QueryResultList(this);
		if (sort == null) {
			result.sort = null;
		} else if (result.sort == null) {
			result.sort = sort;
		} else {
			// TODO: Build the next sort
			result.sort = sort;
		}
		result.initialized = false;
		return result;
	}
	
	public Sort merge(Sort base, Sort delta) {
		return null;
	}

	@Override
	public QueryResultList where(Query criteria) {
		final QueryResultList result = new QueryResultList(this);
		result.query = merge(result.query, criteria);
		result.initialized = false;
		return result;
	}
	
	public Query merge(Query base, Query delta) {
		Query result;
		if (delta == null) {
			result = null;
		} else if (base == null) {
			result = delta;
		} else {
			final BooleanQuery booleanQuery = new BooleanQuery();
			booleanQuery.add(base, BooleanClause.Occur.MUST);
			booleanQuery.add(delta, BooleanClause.Occur.MUST);
			result = booleanQuery;
		}
		return result;
	}

}
