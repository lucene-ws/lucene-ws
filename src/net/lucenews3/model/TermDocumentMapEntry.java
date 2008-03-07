package net.lucenews3.model;

import java.util.Map;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;

public class TermDocumentMapEntry implements Map.Entry<Term, DocumentList> {

	private IndexReader indexReader;
	private Term term;
	private int documentCount;
	private DocumentList documents;
	
	public TermDocumentMapEntry(IndexReader indexReader, Term term, int documentCount) {
		this.indexReader = indexReader;
		this.term = term;
		this.documentCount = documentCount;
		this.documents = new TermDocumentList(indexReader, term, documentCount);
	}
	
	public Term getKey() {
		return term;
	}

	public DocumentList getValue() {
		return documents;
	}

	public DocumentList setValue(DocumentList documents) {
		throw new UnsupportedOperationException();
	}

}
