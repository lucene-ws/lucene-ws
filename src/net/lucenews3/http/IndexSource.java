package net.lucenews3.http;

/**
 * Manages a collection of indexes.
 *
 */
public interface IndexSource {

	public IndexCollection getIndexes();
	
	public Index getIndex(String indexName);
	
}
