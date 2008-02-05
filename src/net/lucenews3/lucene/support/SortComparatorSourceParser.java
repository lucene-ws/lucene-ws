package net.lucenews3.lucene.support;

import org.apache.lucene.search.SortComparatorSource;

public interface SortComparatorSourceParser {

	public SortComparatorSource parse(String string);
	
}
