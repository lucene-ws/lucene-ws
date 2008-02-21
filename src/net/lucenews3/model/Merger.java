package net.lucenews3.model;

/**
 * A generic interface which describes an object capable of merging two objects
 * into one where both inputs and output are of the same class.
 * 
 * @param <T>
 */
public interface Merger<T> {

	/**
	 * Merges the given base and the given delta.
	 * 
	 * @param base
	 * @param delta
	 * @return
	 */
	public T merge(T base, T delta);

}
