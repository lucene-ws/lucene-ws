package net.lucenews3.lucene.support;

import java.io.IOException;
import java.util.AbstractList;

import net.lucenews.http.ExceptionWrapper;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;

public class IndexReaderDocumentList extends AbstractList<Document> implements DocumentList {

	private IndexReader reader;
	private IndexWriter writer;
	private IndexSearcher searcher;
	private Query criteria;
	private ExceptionWrapper exceptionWrapper;
	
	public IndexReaderDocumentList(IndexReaderDocumentList prototype) {
		this.reader = prototype.reader;
		this.writer = prototype.writer;
		this.searcher = prototype.searcher;
		this.criteria = prototype.criteria;
		this.exceptionWrapper = prototype.exceptionWrapper;
	}
	
	public IndexReaderDocumentList(IndexReader reader, IndexWriter writer, IndexSearcher searcher) {
		this.reader = reader;
		this.writer = writer;
		this.searcher = searcher;
	}
	
	/**
	 * Determines the document number associated with
	 * the <code>index</code><sup>th</sup> <em>non-deleted</em>
	 * document. This number is suitable for use with 
	 * {@link IndexReader#document(int)}.
	 * @param index
	 * @return
	 */
	public int indexToDocumentNumber(int index) {
		int currentIndex = 0;
		int currentNumber = 0;
		
		do {
			if (!reader.isDeleted(currentNumber)) {
				currentIndex++;
			}
			currentNumber++;
		} while (currentIndex < index);
		
		return -1;
	}
	
	@Override
	public boolean add(Document document) {
		try {
			writer.addDocument(document);
		} catch (CorruptIndexException e) {
			throw exceptionWrapper.wrap(e);
		} catch (IOException e) {
			throw exceptionWrapper.wrap(e);
		}
		return true;
	}
	
	/**
	 * Retrieves the <code>index</code><sup>th</sup> <em>non-deleted</em>
	 * document. The first existing document is at index 0.
	 */
	@Override
	public Document get(int index) {
		Document result;
		
		try {
			result = reader.document(indexToDocumentNumber(index));
		} catch (CorruptIndexException e) {
			throw exceptionWrapper.wrap(e);
		} catch (IOException e) {
			throw exceptionWrapper.wrap(e);
		}
		
		return result;
	}

	@Override
	public Document remove(int index) {
		return null;
	}
	
	@Override
	public int size() {
		return reader.numDocs();
	}

	/**
	 * Returns an instance of <code>DocumentList</code> representing
	 * a subset of the current collection of documents which meet the
	 * given criteria.
	 * @param criteria a Lucene query to dictate the contents of the sub-list
	 * @return a <code>DocumentList</code> which has been filtered by the given criteria
	 */
	public DocumentList where(Query criteria) {
		IndexReaderDocumentList result = new IndexReaderDocumentList(this);
		if (result.criteria == null) {
			result.criteria = criteria;
		} else {
			BooleanQuery booleanQuery = new BooleanQuery();
			booleanQuery.add(result.criteria, BooleanClause.Occur.MUST);
			booleanQuery.add(criteria, BooleanClause.Occur.MUST);
			result.criteria = booleanQuery;
		}
		return result;
	}

}
