package net.lucenews3.lucene.support;

import org.apache.lucene.search.Query;

public interface QueryParser<I> {

	public Query parseQuery(I input);
	
}
