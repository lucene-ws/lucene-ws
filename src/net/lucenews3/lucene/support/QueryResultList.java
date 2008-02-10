package net.lucenews3.lucene.support;

import java.io.IOException;
import java.util.AbstractList;

import net.lucenews.http.ExceptionWrapper;

import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.Sort;

public class QueryResultList extends AbstractList<Result> implements ResultList {

	private ExceptionWrapper exceptionWrapper;
	private Searcher searcher;
	private QueryParser queryParser;
	private Query query;
	private QueryMerger queryMerger;
	private Filter filter;
	private FilterMerger filterMerger;
	private Sort sort;
	private SortMerger sortMerger;
	private Hits hits;
	private boolean initialized;
	
	public QueryResultList(QueryResultList prototype) {
		this.exceptionWrapper = prototype.exceptionWrapper;
		this.searcher = prototype.searcher;
		this.queryParser = prototype.queryParser;
		this.query = prototype.query;
		this.queryMerger = prototype.queryMerger;
		this.filter = prototype.filter;
		this.filterMerger = prototype.filterMerger;
		this.sort = prototype.sort;
		this.sortMerger = prototype.sortMerger;
		this.hits = prototype.hits;
		this.initialized = prototype.initialized;
	}
	
	public QueryResultList() {
		this.exceptionWrapper = new DefaultExceptionWrapper();
		this.filterMerger = new FilterMergerImpl();
		this.queryMerger = new QueryMergerImpl();
		this.sortMerger = new SortMergerImpl();
		this.initialized = false;
	}
	
	public QueryResultList(Searcher searcher) {
		this(searcher, new MatchAllDocsQuery());
	}
	
	public QueryResultList(Searcher searcher, Query query) {
		this();
		this.searcher = searcher;
		this.query = query;
	}
	
	public QueryResultList(Searcher searcher, QueryParser queryParser) {
		this();
		this.searcher = searcher;
		this.queryParser = queryParser;
	}
	
	public ExceptionWrapper getExceptionWrapper() {
		return exceptionWrapper;
	}

	public void setExceptionWrapper(ExceptionWrapper exceptionWrapper) {
		this.exceptionWrapper = exceptionWrapper;
	}

	public Filter getFilter() {
		return filter;
	}

	public void setFilter(Filter filter) {
		this.filter = filter;
	}

	public FilterMerger getFilterMerger() {
		return filterMerger;
	}

	public void setFilterMerger(FilterMerger filterMerger) {
		this.filterMerger = filterMerger;
	}

	public Hits getHits() {
		return hits;
	}

	public void setHits(Hits hits) {
		this.hits = hits;
	}

	public boolean isInitialized() {
		return initialized;
	}

	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

	public Query getQuery() {
		return query;
	}

	public void setQuery(Query query) {
		this.query = query;
	}

	public QueryMerger getQueryMerger() {
		return queryMerger;
	}

	public void setQueryMerger(QueryMerger queryMerger) {
		this.queryMerger = queryMerger;
	}

	public QueryParser getQueryParser() {
		return queryParser;
	}

	public void setQueryParser(QueryParser queryParser) {
		this.queryParser = queryParser;
	}

	public Searcher getSearcher() {
		return searcher;
	}

	public void setSearcher(Searcher searcher) {
		this.searcher = searcher;
	}

	public Sort getSort() {
		return sort;
	}

	public void setSort(Sort sort) {
		this.sort = sort;
	}

	public SortMerger getSortMerger() {
		return sortMerger;
	}

	public void setSortMerger(SortMerger sortMerger) {
		this.sortMerger = sortMerger;
	}

	public void initialize() {
		if (!initialized) {
			if (query == null) {
				query = new MatchAllDocsQuery();
			}
			try {
				hits = searcher.search(query, filter, sort);
			} catch (IOException e) {
				throw exceptionWrapper.wrap(e);
			}
			initialized = true;
		}
	}
	
	@Override
	public Result get(int index) {
		initialize();
		return new ResultImpl(hits, index, exceptionWrapper);
	}

	@Override
	public int size() {
		initialize();
		return hits.length();
	}

	@Override
	public QueryResultList filteredBy(Filter filter) {
		final QueryResultList result = new QueryResultList(this);
		result.filter = filterMerger.mergeFilters(result.filter, filter);
		result.initialized = false;
		return result;
	}

	@Override
	public QueryResultList sortedBy(Sort sort) {
		final QueryResultList result = new QueryResultList(this);
		result.sort = sortMerger.mergeSorts(result.sort, sort);
		result.initialized = false;
		return result;
	}

	@Override
	public QueryResultList where(Query criteria) {
		final QueryResultList result = new QueryResultList(this);
		result.query = queryMerger.mergeQueries(result.query, criteria);
		result.initialized = false;
		return result;
	}
	
	@Override
	public QueryResultList where(String criteria) {
		try {
			return where(queryParser.parse(criteria));
		} catch (ParseException e) {
			throw exceptionWrapper.wrap(e);
		}
	}

}
