package net.lucenews3.model.servlet;

public interface PathClass {
	
	public Path parse(Iterable<String> tokens) throws PathParseException;
	
}
