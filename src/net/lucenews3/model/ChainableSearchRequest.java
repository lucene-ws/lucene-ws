package net.lucenews3.model;

import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;

public interface ChainableSearchRequest extends SearchRequest {

	public ChainableSearchRequest where(Query query);
	
	public ChainableSearchRequest where(String query);
	
	public ChainableSearchRequest filteredBy(Filter filter);
	
	public ChainableSearchRequest sortedBy(Sort sort);
	
	

	public QueryParser getQueryParser();
	
	public void setQueryParser(QueryParser queryParser);
	
	public QueryMerger getQueryMerger();
	
	public void setQueryMerger(QueryMerger queryMerger);
	
	public FilterMerger getFilterMerger();
	
	public void setFilterMerger(FilterMerger filterMerger);
	
	public SortMerger getSortMerger();
	
	public void setSortMerger(SortMerger sortMerger);
	
}
