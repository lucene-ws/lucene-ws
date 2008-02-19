package net.lucenews.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import net.lucenews3.ExceptionTranslator;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.MultiReader;
import org.apache.lucene.search.MultiSearcher;
import org.apache.lucene.search.Searcher;

public class DefaultCompositeIndex implements CompositeIndex {

	private Collection<Index> indexes;
	private boolean closeSubReaders;
	private ExceptionTranslator exceptionTranslator;
	
	public DefaultCompositeIndex() {
		
	}
	
	public DefaultCompositeIndex(Collection<Index> indexes) {
		this.indexes = indexes;
	}
	
	public Collection<Index> getIndexes() {
		return indexes;
	}
	
	public void setIndexes(Collection<Index> indexes) {
		this.indexes = indexes;
	}

	public boolean isCloseSubReaders() {
		return closeSubReaders;
	}

	public void setCloseSubReaders(boolean closeSubReaders) {
		this.closeSubReaders = closeSubReaders;
	}

	public ExceptionTranslator getExceptionTranslator() {
		return exceptionTranslator;
	}

	public void setExceptionTranslator(ExceptionTranslator exceptionTranslator) {
		this.exceptionTranslator = exceptionTranslator;
	}

	/**
	 * Returns a collection of <code>IndexReader</code>s obtained
	 * from the various indexes.
	 * @return
	 * @see Index#getIndexReader()
	 */
	public Collection<IndexReader> getIndexReaders() {
		Collection<IndexReader> indexReaders = new ArrayList<IndexReader>(indexes.size());
		for (Index index : indexes) {
			IndexReader indexReader = index.getIndexReader();
			indexReaders.add(indexReader);
		}
		return indexReaders;
	}
	
	/**
	 * 
	 * @see MultiReader
	 */
	@Override
	public IndexReader getIndexReader() {
		return new MultiReader(getIndexReaders().toArray(new IndexReader[]{}), closeSubReaders);
	}
	
	/**
	 * Retrieves a collection of index writers, one per index.
	 * @return
	 * @see Index#getIndexWriter()
	 */
	public Collection<IndexWriter> getIndexWriters() {
		Collection<IndexWriter> indexWriters = new ArrayList<IndexWriter>(indexes.size());
		for (Index index : indexes) {
			IndexWriter indexWriter = index.getIndexWriter();
			indexWriters.add(indexWriter);
		}
		return indexWriters;
	}

	@Override
	public IndexWriter getIndexWriter() {
		throw new RuntimeException("Cannot write to a composite index");
	}
	
	/**
	 * Retrieves a collection of searchers, one from each index.
	 * @return
	 * @see Index#getSearcher()
	 */
	public Collection<Searcher> getSearchers() {
		Collection<Searcher> searchers = new ArrayList<Searcher>(indexes.size());
		for (Index index : indexes) {
			Searcher searcher = index.getSearcher();
			searchers.add(searcher);
		}
		return searchers;
	}
	
	/**
	 * 
	 * @see MultiSearcher
	 */
	@Override
	public Searcher getSearcher() {
		try {
			return new MultiSearcher(getSearchers().toArray(new Searcher[]{}));
		} catch (IOException e) {
			throw exceptionTranslator.translate(e);
		}
	}

}
