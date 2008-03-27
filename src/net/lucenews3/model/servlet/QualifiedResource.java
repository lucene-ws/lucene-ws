package net.lucenews3.model.servlet;

public interface QualifiedResource<R> {

	public Path getPath();
	
	public R getResource();
	
}
