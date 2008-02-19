package net.lucenews3.lucene.support;

import java.io.IOException;

import net.lucenews3.ExceptionTranslator;
import net.lucenews3.lucene.IndexReader;
import net.lucenews3.lucene.IndexReaderImpl;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.store.Directory;

public class IndexImpl implements Index {

	private Directory directory;
	private IndexMetaData metaData;
	private ExceptionTranslator exceptionTranslator;

	public IndexImpl(Directory directory) {
		this.directory = directory;
	}

	public IndexImpl(Directory directory, IndexMetaData metaData) {
		this.directory = directory;
		this.metaData = metaData;
	}

	public IndexImpl(IndexMetaData metaData) {
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
			return new IndexReaderImpl(org.apache.lucene.index.IndexReader.open(directory));
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

}
