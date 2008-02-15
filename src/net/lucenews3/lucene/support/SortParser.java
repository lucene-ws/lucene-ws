package net.lucenews3.lucene.support;

import org.apache.lucene.search.Sort;

public interface SortParser<I> {

	public Sort parseSort(I input);
	
}
