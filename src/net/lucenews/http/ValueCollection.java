package net.lucenews.http;

import java.util.Collection;

public interface ValueCollection<V> extends Collection<V> {

	/**
	 * Obtains the first value in this collection.
	 * @return
	 */
	public V first();
	
	/**
	 * Obtains the only value in this collection. This
	 * method will throw an exception if the collection
	 * does not have precisely one value.
	 * @return
	 */
	public V only();
	
}
