package net.lucenews3.model;

import java.io.IOException;
import java.util.Iterator;

import net.lucenews3.ExceptionTranslator;
import net.lucenews3.ExceptionTranslatorImpl;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermDocs;

public class TermDocumentsIterator implements Iterator<Document> {

	private IndexReader indexReader;
	private Term term;
	private TermDocs enumerator;
	private Boolean hasNext;
	private ExceptionTranslator exceptionTranslator;
	
	public TermDocumentsIterator(IndexReader indexReader, Term term) {
		this.indexReader = indexReader;
		this.term = term;
		this.exceptionTranslator = new ExceptionTranslatorImpl();
	}
	
	@Override
	public boolean hasNext() {
		if (hasNext == null) {
			try {
				hasNext = enumerator.next();
			} catch (IOException e) {
				throw exceptionTranslator.translate(e);
			}
			
			
		}
		
		return hasNext;
	}

	@Override
	public Document next() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remove() {
		// TODO Auto-generated method stub
		
	}

}
