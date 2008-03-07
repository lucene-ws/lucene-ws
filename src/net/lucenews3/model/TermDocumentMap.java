package net.lucenews3.model;

import java.util.Map;

import org.apache.lucene.index.Term;

public interface TermDocumentMap extends Map<Term, DocumentList> {

	public TermList keyList();
	
}
