package net.lucenews3.lucene.support;

import org.apache.lucene.search.SortField;

public interface SortFieldParser<I> {

	public SortField parseSortField(I input);
	
}
