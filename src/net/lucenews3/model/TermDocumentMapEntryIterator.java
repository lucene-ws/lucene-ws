package net.lucenews3.model;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermEnum;

public class TermDocumentMapEntryIterator implements Iterator<Map.Entry<Term, DocumentList>> {

	private TermEnum enumeration;
	
	public TermDocumentMapEntryIterator(TermEnum enumeration) {
		this.enumeration = enumeration;
	}
	
	@Override
	public boolean hasNext() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Entry<Term, DocumentList> next() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remove() {
		// TODO Auto-generated method stub
		
	}

}
