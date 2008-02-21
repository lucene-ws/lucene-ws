package net.lucenews3.model;

import org.apache.lucene.search.Sort;

public interface SortParser<I> extends Parser<I, Sort> {

	@Override
	public Sort parse(I input);
	
}
