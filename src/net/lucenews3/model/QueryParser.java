package net.lucenews3.model;

import org.apache.lucene.search.Query;

public interface QueryParser<I> extends Parser<I, Query> {

	@Override
	public Query parse(I input);
	
}
