package net.lucenews.http;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.Searcher;

public interface Index {
	
	/**
	 * Obtains a new index reader for this particular index.
	 * @return
	 */
	public IndexReader getIndexReader();
	
	/**
	 * Retrieves a new index writer for this particular index.
	 * @return
	 */
	public IndexWriter getIndexWriter();
	
	/**
	 * Obtains a new index searcher for this particular index.
	 * @return
	 */
	public Searcher getSearcher();
	
}
