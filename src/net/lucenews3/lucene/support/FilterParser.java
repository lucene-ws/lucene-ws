package net.lucenews3.lucene.support;

import org.apache.lucene.search.Filter;

public interface FilterParser<I> {

	public Filter parseFilter(I input);
	
}
