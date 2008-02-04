package net.lucenews3.lucene.support;

import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;

public class QueryMergerImpl implements QueryMerger {

	public Query mergeQueries(Query base, Query delta) {
		Query result;
		if (delta == null) {
			result = null;
		} else if (base == null) {
			result = delta;
		} else {
			final BooleanQuery booleanQuery = new BooleanQuery();
			booleanQuery.add(base, BooleanClause.Occur.MUST);
			booleanQuery.add(delta, BooleanClause.Occur.MUST);
			result = booleanQuery;
		}
		return result;
	}

}
