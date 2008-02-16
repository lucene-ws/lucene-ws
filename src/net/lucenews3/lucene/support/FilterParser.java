package net.lucenews3.lucene.support;

import org.apache.lucene.search.Filter;

public interface FilterParser<I> extends Parser<I, Filter> {

	@Override
	public Filter parse(I input);
	
}
