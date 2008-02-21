package net.lucenews3;

import net.lucenews3.model.Index;

/**
 * An interface to the Lucene Web Service.
 *
 */
public interface Service {
	
	public Index getIndex(String key);
	
}
