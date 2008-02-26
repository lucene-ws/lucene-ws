package net.lucenews3.model;

import org.apache.lucene.search.Query;

/**
 * Represents a query which is decorating another.
 * 
 * Does <strong>not</strong> represent a query which
 * filters its results. 
 *
 */
public interface FilterQuery {

	public Query getTarget();
	
}
