package net.lucenews3.model;

import org.apache.lucene.search.Query;

public interface QueryMerger extends Merger<Query> {

	@Override
	public Query merge(Query base, Query delta);
	
}
