package net.lucenews3.model;

import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;

public class QueryMergerImpl implements QueryMerger {

	@Override
	public Query merge(Query base, Query delta) {
		Query query;
		
		if (delta == null) {
			query = null;
		} else if (base == null) {
			query = delta;
		} else {
			BooleanQuery booleanQuery = new BooleanQuery();
			booleanQuery.add(base, BooleanClause.Occur.MUST);
			booleanQuery.add(delta, BooleanClause.Occur.MUST);
			query = booleanQuery;
		}
		
		return query;
	}

}
