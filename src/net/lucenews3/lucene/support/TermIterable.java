package net.lucenews3.lucene.support;

import org.apache.lucene.index.Term;

public interface TermIterable extends Iterable<Term> {

	public TermIterator iterator();
	
}
