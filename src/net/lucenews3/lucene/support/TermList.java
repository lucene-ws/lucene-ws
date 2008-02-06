package net.lucenews3.lucene.support;

import java.util.List;

import org.apache.lucene.index.Term;

public interface TermList extends List<Term> {

	public TermList subList(Term from, Term to);
	
}
