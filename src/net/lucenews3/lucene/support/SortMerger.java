package net.lucenews3.lucene.support;

import org.apache.lucene.search.Sort;

public interface SortMerger extends Merger<Sort> {

	@Override
	public Sort merge(Sort base, Sort delta);
	
}
