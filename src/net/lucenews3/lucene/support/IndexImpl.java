package net.lucenews3.lucene.support;

import java.io.IOException;

import net.lucenews.http.ExceptionWrapper;
import net.lucenews3.lucene.IndexReader;
import net.lucenews3.lucene.IndexReaderImpl;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.store.Directory;

public class IndexImpl implements Index {

	private Directory directory;
	private ExceptionWrapper exceptionWrapper;

	public IndexImpl(Directory directory) {
		this.directory = directory;
	}
	
	public DocumentList getDocuments() {
		// TODO Auto-generated method stub
		return null;
	}

	public IndexReader getIndexReader() {
		try {
			return new IndexReaderImpl(org.apache.lucene.index.IndexReader.open(directory));
		} catch (CorruptIndexException e) {
			throw exceptionWrapper.wrap(e);
		} catch (IOException e) {
			throw exceptionWrapper.wrap(e);
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
		// TODO Auto-generated method stub
		return null;
	}

}
