package net.lucenews3.model;

import java.util.Iterator;

import org.apache.lucene.index.Term;

public class BufferedTermListIterator extends BufferedListIterator<Term> implements TermListIterator {

	public BufferedTermListIterator(Iterator<Term> iterator) {
		super(iterator);
	}

	@Override
	public void skipTo(Term target) {
		// TODO Auto-generated method stub
		
	}

}
