package net.lucenews3.model;

public interface Visitor<T> {

	public Object visit(T target);
	
}
