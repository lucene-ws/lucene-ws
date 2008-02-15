package net.lucenews3.lucene.support;

import org.apache.lucene.search.Filter;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.Sort;

public interface SearchRequest {

	public Query getQuery();
	
	public void setQuery(Query query);
	
	public Filter getFilter();
	
	public void setFilter(Filter filter);
	
	public Sort getSort();
	
	public void setSort(Sort sort);
	
	public Hits search(Searcher searcher);
	
}
