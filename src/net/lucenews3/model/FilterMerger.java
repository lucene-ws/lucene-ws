package net.lucenews3.model;

import org.apache.lucene.search.Filter;

public interface FilterMerger extends Merger<Filter> {

	@Override
	public Filter merge(Filter base, Filter delta);
	
}
