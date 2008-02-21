package net.lucenews3.model;

import org.apache.lucene.search.Sort;

public interface SortMerger extends Merger<Sort> {

	@Override
	public Sort merge(Sort base, Sort delta);
	
}
