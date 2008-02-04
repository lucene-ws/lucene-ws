package net.lucenews3.lucene.support;

import org.apache.lucene.search.Query;

public interface QueryMerger {

	public Query mergeQueries(Query base, Query delta);
	
}
