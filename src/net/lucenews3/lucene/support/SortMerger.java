package net.lucenews3.lucene.support;

import org.apache.lucene.search.Sort;

public interface SortMerger {

	public Sort mergeSorts(Sort base, Sort delta);
	
}
