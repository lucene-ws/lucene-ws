package net.lucenews3.lucene.support;

import java.io.IOException;
import java.lang.ref.WeakReference;

import net.lucenews.http.ExceptionWrapper;

import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermEnum;

public class TermEnumListIterator implements TermListIterator {

	private ExceptionWrapper exceptionWrapper;
	private TermEnum enumeration;
	private Boolean hasNext;
	private Link first;
	private Link last;
	private Link current;
	
	public TermEnumListIterator(TermEnum enumeration) {
		this.enumeration = enumeration;
	}
	
	protected class Link {
		public int index;
		public Term term;
		public WeakReference<Link> previous;
		public WeakReference<Link> next;
	}

	@Override
	public void skipTo(Term target) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean hasNext() {
		if (hasNext == null) {
			try {
				if (enumeration.next()) {
					
				} else {
					
				}
			} catch (IOException e) {
				throw exceptionWrapper.wrap(e);
			}
		}
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Term next() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void add(Term term) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean hasPrevious() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int nextIndex() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Term previous() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int previousIndex() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void set(Term term) {
		throw new UnsupportedOperationException();
	}
	
}
