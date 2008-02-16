package net.lucenews3.lucene.support;

import org.apache.lucene.search.Query;

public interface QueryParser<I> extends Parser<I, Query> {

	@Override
	public Query parse(I input);
	
}
