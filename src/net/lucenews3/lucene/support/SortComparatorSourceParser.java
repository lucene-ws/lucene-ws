package net.lucenews3.lucene.support;

import org.apache.lucene.search.SortComparatorSource;

public interface SortComparatorSourceParser<I> extends Parser<I, SortComparatorSource> {

	@Override
	public SortComparatorSource parse(I input);
	
}
