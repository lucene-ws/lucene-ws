package net.lucenews3.model.servlet;

/**
 * Resolves a resource from an abstract path.
 *
 */
public interface ResourceResolver {

	public Object resolveResource(Iterable<String> tokens);
	
}
