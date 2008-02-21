package net.lucenews3.model;

import org.apache.lucene.search.Query;

public interface QueryVisitor {

	public Object visit(Query query);
	
}
