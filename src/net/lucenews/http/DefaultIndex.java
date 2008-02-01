package net.lucenews.http;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexDeletionPolicy;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;

/**
 * The default implementation of <code>Index</code>.
 *
 */
public class DefaultIndex implements Index {

	private Directory directory;
	private boolean autoCommit;
	private Analyzer analyzer;
	private boolean autoCreate;
	private IndexDeletionPolicy deletionPolicy;
	private ExceptionWrapper exceptionWrapper;
	
	public DefaultIndex() {
		
	}
	
	public DefaultIndex(Directory directory, Analyzer analyzer) {
		this.directory = directory;
		this.analyzer = analyzer;
	}
	
	/**
	 * Opens a new <code>IndexReader</code> via {@link IndexReader#open(Directory)}.
	 */
	@Override
	public IndexReader getIndexReader() {
		try {
			return IndexReader.open(directory);
		} catch (CorruptIndexException e) {
			throw exceptionWrapper.wrap(e);
		} catch (IOException e) {
			throw exceptionWrapper.wrap(e);
		}
	}

	@Override
	public IndexWriter getIndexWriter() {
		try {
			return new IndexWriter(directory, autoCommit, analyzer, autoCreate, deletionPolicy);
		} catch (CorruptIndexException e) {
			throw exceptionWrapper.wrap(e);
		} catch (LockObtainFailedException e) {
			throw exceptionWrapper.wrap(e);
		} catch (IOException e) {
			throw exceptionWrapper.wrap(e);
		}
	}
	
	@Override
	public Searcher getSearcher() {
		try {
			return new IndexSearcher(directory);
		} catch (CorruptIndexException e) {
			throw exceptionWrapper.wrap(e);
		} catch (IOException e) {
			throw exceptionWrapper.wrap(e);
		}
	}
	
	public Directory getDirectory() {
		return directory;
	}
	
	public void setDirectory(Directory directory) {
		this.directory = directory;
	}

	public boolean isAutoCommit() {
		return autoCommit;
	}

	public void setAutoCommit(boolean autoCommit) {
		this.autoCommit = autoCommit;
	}

	public Analyzer getAnalyzer() {
		return analyzer;
	}

	public void setAnalyzer(Analyzer analyzer) {
		this.analyzer = analyzer;
	}

	public boolean isAutoCreate() {
		return autoCreate;
	}

	public void setAutoCreate(boolean autoCreate) {
		this.autoCreate = autoCreate;
	}

	public IndexDeletionPolicy getDeletionPolicy() {
		return deletionPolicy;
	}

	public void setDeletionPolicy(IndexDeletionPolicy deletionPolicy) {
		this.deletionPolicy = deletionPolicy;
	}

	public ExceptionWrapper getExceptionWrapper() {
		return exceptionWrapper;
	}

	public void setExceptionWrapper(ExceptionWrapper exceptionWrapper) {
		this.exceptionWrapper = exceptionWrapper;
	}

}
