package net.lucenews3.model;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.index.Term;

public class TermDocumentMapImpl extends AbstractMap<Term, DocumentList> implements TermDocumentMap {

	@Override
	public Set<java.util.Map.Entry<Term, DocumentList>> entrySet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TermList keyList() {
		// TODO Auto-generated method stub
		return null;
	}

	public DocumentList put(Term term, DocumentList documents) {
		throw new UnsupportedOperationException();
	}

	public void putAll(Map<? extends Term, ? extends DocumentList> map) {
		throw new UnsupportedOperationException();
	}

}
