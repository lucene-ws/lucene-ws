package net.lucenews3.model;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import net.lucenews3.ExceptionTranslator;
import net.lucenews3.ExceptionTranslatorImpl;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermDocs;

public class TermDocumentIterator implements Iterator<Document> {

	private IndexReader indexReader;
	private TermDocs termDocs;
	private Boolean hasNext;
	private ExceptionTranslator exceptionTranslator;
	private Term fromTerm;
	private Term toTerm;
	
	/**
	 * Constructors an iterator which will only iterate over those documents
	 * which contain this term.
	 * @param indexReader
	 * @param term
	 */
	public TermDocumentIterator(IndexReader indexReader, Term term) {
		this(indexReader, term, (Term) null);
	}
	
	/**
	 * Constructs an iterator which will iterate over the terms which are greater
	 * than or equal to "fromTerm" and less than "toTerm".
	 * @param indexReader
	 * @param fromTerm (may be null to indicate no lower bound)
	 * @param toTerm (may be null to indicate no upper bound)
	 */
	public TermDocumentIterator(IndexReader indexReader, Term fromTerm, Term toTerm) {
		this.indexReader = indexReader;
		this.fromTerm = fromTerm;
		this.toTerm = toTerm;
	}
	
	public TermDocumentIterator(IndexReader indexReader, TermDocs termDocs) {
		this.indexReader = indexReader;
		this.termDocs = termDocs;
		this.exceptionTranslator = new ExceptionTranslatorImpl();
	}
	
	@Override
	public boolean hasNext() {
		while (hasNext == null) {
			if (termDocs == null) {
				try {
					termDocs = indexReader.termDocs(fromTerm);
				} catch (IOException e) {
					throw exceptionTranslator.translate(e);
				}
			}
			
			try {
				hasNext = termDocs.next();
			} catch (IOException e) {
				throw exceptionTranslator.translate(e);
			}
		}
		
		return hasNext;
	}

	@Override
	public Document next() {
		Document result;
		
		if (hasNext()) {
			result = new ReferencedNativeDocument(indexReader, termDocs.doc());
			hasNext = null;
		} else {
			throw new NoSuchElementException();
		}
		
		return result;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

}
