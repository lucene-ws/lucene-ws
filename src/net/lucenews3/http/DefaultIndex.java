package net.lucenews3.http;

import java.io.IOException;

import net.lucenews3.ExceptionTranslator;

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
	private ExceptionTranslator exceptionTranslator;
	
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
			throw exceptionTranslator.translate(e);
		} catch (IOException e) {
			throw exceptionTranslator.translate(e);
		}
	}

	@Override
	public IndexWriter getIndexWriter() {
		try {
			return new IndexWriter(directory, autoCommit, analyzer, autoCreate, deletionPolicy);
		} catch (CorruptIndexException e) {
			throw exceptionTranslator.translate(e);
		} catch (LockObtainFailedException e) {
			throw exceptionTranslator.translate(e);
		} catch (IOException e) {
			throw exceptionTranslator.translate(e);
		}
	}
	
	@Override
	public Searcher getSearcher() {
		try {
			return new IndexSearcher(directory);
		} catch (CorruptIndexException e) {
			throw exceptionTranslator.translate(e);
		} catch (IOException e) {
			throw exceptionTranslator.translate(e);
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

	public ExceptionTranslator getExceptionTranslator() {
		return exceptionTranslator;
	}

	public void setExceptionTranslator(ExceptionTranslator exceptionTranslator) {
		this.exceptionTranslator = exceptionTranslator;
	}

}
