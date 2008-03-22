package net.lucenews3.model;

/**
 * Responsible for producing clones of objects.
 *
 */
public interface Cloner {

	public <T> T clone(T source);
	
}
