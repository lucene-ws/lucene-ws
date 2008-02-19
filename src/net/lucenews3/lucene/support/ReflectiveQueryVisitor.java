package net.lucenews3.lucene.support;

import net.lucenews3.ExceptionTranslator;

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
	
	public ReflectiveQueryVisitor(ExceptionTranslator exceptionTranslator) {
		super(exceptionTranslator);
	}
	
}
