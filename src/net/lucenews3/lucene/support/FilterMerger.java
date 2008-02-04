package net.lucenews3.lucene.support;

import org.apache.lucene.search.Filter;

public interface FilterMerger {

	public Filter mergeFilters(Filter base, Filter delta);
	
}
