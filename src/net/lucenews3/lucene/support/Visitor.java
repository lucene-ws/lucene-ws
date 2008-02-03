package net.lucenews3.lucene.support;

public interface Visitor<T> {

	public Object visit(T target);
	
}
