package net.lucenews3.lucene.support;

import net.lucenews.http.ExceptionWrapper;

import org.apache.lucene.search.Query;

/**
 * A query visitor which shortens the workload
 * by taking advantage of Java reflection.
 *
 */
public class ReflectiveQueryVisitor extends ReflectiveVisitor<Query> implements QueryVisitor {
	
	public ReflectiveQueryVisitor() {
		super();
	}
	
	public ReflectiveQueryVisitor(ExceptionWrapper exceptionWrapper) {
		super(exceptionWrapper);
	}
	
}
