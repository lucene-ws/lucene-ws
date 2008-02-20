package net.lucenews3.lucene.support;

import java.util.AbstractList;

import net.lucenews3.ExceptionTranslator;
import net.lucenews3.ExceptionTranslatorImpl;

import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.Sort;

public class QueryResultList extends AbstractList<Result> implements ResultList {

	private ExceptionTranslator exceptionTranslator;
	private Searcher searcher;
	private QueryParser queryParser;
	private SearchRequest searchRequest;
	private QueryMerger queryMerger;
	private FilterMerger filterMerger;
	private SortMerger sortMerger;
	private Hits hits;
	private boolean initialized;
	
	public QueryResultList(QueryResultList prototype) {
		this.exceptionTranslator = prototype.exceptionTranslator;
		this.searcher = prototype.searcher;
		this.queryParser = prototype.queryParser;
		this.searchRequest = prototype.searchRequest;
		this.queryMerger = prototype.queryMerger;
		this.filterMerger = prototype.filterMerger;
		this.sortMerger = prototype.sortMerger;
		this.hits = prototype.hits;
		this.initialized = prototype.initialized;
	}
	
	public QueryResultList() {
		this.exceptionTranslator = new ExceptionTranslatorImpl();
		this.filterMerger = new FilterMergerImpl();
		this.queryMerger = new QueryMergerImpl();
		this.sortMerger = new SortMergerImpl();
		this.initialized = false;
		this.searchRequest = new SearchRequestImpl();
	}
	
	public QueryResultList(Searcher searcher) {
		this(searcher, new MatchAllDocsQuery());
	}
	
	public QueryResultList(Searcher searcher, Query query) {
		this();
		this.searcher = searcher;
		this.searchRequest.setQuery(query);
	}
	
	public QueryResultList(Searcher searcher, QueryParser queryParser) {
		this();
		this.searcher = searcher;
		this.queryParser = queryParser;
	}
	
	public QueryResultList(Searcher searcher, SearchRequest searchRequest) {
		this();
		this.searcher = searcher;
	}
	
	public ExceptionTranslator getExceptionTranslator() {
		return exceptionTranslator;
	}

	public void setExceptionTranslator(ExceptionTranslator exceptionTranslator) {
		this.exceptionTranslator = exceptionTranslator;
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

	public SearchRequest getSearchRequest() {
		return searchRequest;
	}

	public void setSearchRequest(SearchRequest searchRequest) {
		this.searchRequest = searchRequest;
	}

	public SortMerger getSortMerger() {
		return sortMerger;
	}

	public void setSortMerger(SortMerger sortMerger) {
		this.sortMerger = sortMerger;
	}

	public void initialize() {
		if (!initialized) {
			if (searchRequest == null) {
				searchRequest = new SearchRequestImpl(new MatchAllDocsQuery());
			}
			if (searchRequest.getQuery() == null) {
				searchRequest.setQuery(new MatchAllDocsQuery());
			}
			hits = searchRequest.search(searcher);
			initialized = true;
		}
	}
	
	@Override
	public Result get(int index) {
		initialize();
		return new ResultImpl(hits, index, exceptionTranslator);
	}

	@Override
	public int size() {
		initialize();
		return hits.length();
	}

	@Override
	public QueryResultList filteredBy(Filter filter) {
		final QueryResultList result = new QueryResultList(this);
		final Filter base = result.searchRequest.getFilter();
		final Filter delta = filter;
		result.searchRequest.setFilter(filterMerger.merge(base, delta));
		result.initialized = false;
		return result;
	}

	@Override
	public QueryResultList sortedBy(Sort sort) {
		final QueryResultList result = new QueryResultList(this);
		final Sort base = result.searchRequest.getSort();
		final Sort delta = sort;
		result.searchRequest.setSort(sortMerger.merge(base, delta));
		result.initialized = false;
		return result;
	}

	@Override
	public QueryResultList where(Query criteria) {
		final QueryResultList result = new QueryResultList(this);
		final Query base = result.searchRequest.getQuery();
		final Query delta = criteria;
		result.searchRequest.setQuery(queryMerger.merge(base, delta));
		result.initialized = false;
		return result;
	}
	
	@Override
	public QueryResultList where(String criteria) {
		try {
			return where(queryParser.parse(criteria));
		} catch (ParseException e) {
			throw exceptionTranslator.translate(e);
		}
	}
	
	@Override
	public QueryResultList subList(int fromIndex, int toIndex) {
		return null;
	}

}
