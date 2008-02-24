package net.lucenews3.model;

import java.io.IOException;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import net.lucenews3.ExceptionTranslator;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.store.Directory;

public class IndexImpl implements Index {

	private Directory directory;
	private IndexMetaData metaData;
	private ReadWriteLock dataLock;
	private ExceptionTranslator exceptionTranslator;

	public IndexImpl() {
		this.dataLock = new ReentrantReadWriteLock();
	}
	
	public IndexImpl(Directory directory) {
		this();
		this.directory = directory;
	}

	public IndexImpl(Directory directory, IndexMetaData metaData) {
		this();
		this.directory = directory;
		this.metaData = metaData;
	}

	public IndexImpl(IndexMetaData metaData) {
		this();
		this.metaData = metaData;
	}
	
	public Directory getDirectory() {
		return directory;
	}

	public void setDirectory(Directory directory) {
		this.directory = directory;
	}

	public ExceptionTranslator getExceptionTranslator() {
		return exceptionTranslator;
	}

	public void setExceptionTranslator(ExceptionTranslator exceptionTranslator) {
		this.exceptionTranslator = exceptionTranslator;
	}

	public void setMetaData(IndexMetaData metaData) {
		this.metaData = metaData;
	}

	public DocumentList getDocuments() {
		try {
			return new IndexReaderDocumentList(org.apache.lucene.index.IndexReader.open(directory), null, null);
		} catch (CorruptIndexException e) {
			throw exceptionTranslator.translate(e);
		} catch (IOException e) {
			throw exceptionTranslator.translate(e);
		}
	}

	public IndexReader getIndexReader() {
		try {
			return IndexReader.open(directory);
		} catch (CorruptIndexException e) {
			throw exceptionTranslator.translate(e);
		} catch (IOException e) {
			throw exceptionTranslator.translate(e);
		}
	}

	public IndexWriter getIndexWriter() {
		// TODO Auto-generated method stub
		return null;
	}

	public Searcher getSearcher() {
		// TODO Auto-generated method stub
		return null;
	}

	public TermList getTerms() {
		// TODO Auto-generated method stub
		return null;
	}

	public void close() {
		// TODO Auto-generated method stub
		
	}

	public IndexMetaData getMetaData() {
		return metaData;
	}

	@Override
	public ReadWriteLock getDataLock() {
		return dataLock;
	}

}
