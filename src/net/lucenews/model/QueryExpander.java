package net.lucenews.model;

import org.apache.lucene.search.Query;

public interface QueryExpander {

	public Query expand(Query query);
	
}
