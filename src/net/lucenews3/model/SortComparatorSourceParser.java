package net.lucenews3.model;

import org.apache.lucene.search.SortComparatorSource;

public interface SortComparatorSourceParser<I> extends Parser<I, SortComparatorSource> {

	@Override
	public SortComparatorSource parse(I input);
	
}
