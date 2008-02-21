package net.lucenews3.model;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.Searcher;

import net.lucenews3.Closeable;

/**
 * Represents an index which contains documents and terms.
 *
 */
public interface Index extends Closeable {

	public IndexMetaData getMetaData();
	
	/**
	 * Returns a list of documents contained in this index.
	 * @return
	 */
	public DocumentList getDocuments();
	
	/**
	 * Returns a list of terms contained in this index.
	 * @return
	 */
	public TermList getTerms();
	
	public IndexReader getIndexReader();
	
	public IndexWriter getIndexWriter();
	
	public Searcher getSearcher();
	
}
