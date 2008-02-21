package net.lucenews3.model;

import java.util.List;

import org.apache.lucene.index.Term;

public interface TermList extends List<Term>, TermIterable {

	public TermListIterator listIterator();
	
	public TermListIterator listIterator(int index);
	
	public TermListIterator listIterator(Term term);
	
	public TermList subList(int fromIndex, int toIndex);
	
	public TermList subList(Term fromTerm, Term toTerm);
	
}
