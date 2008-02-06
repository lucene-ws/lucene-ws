package net.lucenews3.lucene.support;

import java.util.Iterator;

import org.apache.lucene.index.Term;

public interface TermIterator extends Iterator<Term> {

	public void skipTo(Term target);
	
}
