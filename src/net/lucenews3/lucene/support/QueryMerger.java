package net.lucenews3.lucene.support;

import org.apache.lucene.search.Query;

public interface QueryMerger extends Merger<Query> {

	@Override
	public Query merge(Query base, Query delta);
	
}
