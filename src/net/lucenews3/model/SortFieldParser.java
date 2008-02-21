package net.lucenews3.model;

import org.apache.lucene.search.SortField;

public interface SortFieldParser<I> extends Parser<I, SortField> {

	@Override
	public SortField parse(I input);
	
}
