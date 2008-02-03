package net.lucenews3.lucene.support;

import org.apache.lucene.search.Query;

public interface QueryVisitor {

	public Object visit(Query query);
	
}
