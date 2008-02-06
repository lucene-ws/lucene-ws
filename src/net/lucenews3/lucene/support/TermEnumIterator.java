package net.lucenews3.lucene.support;

import java.io.IOException;
import java.util.NoSuchElementException;

import net.lucenews.http.ExceptionWrapper;

import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermEnum;

/**
 * A term iterator which uses a <code>TermEnum</code> object as its source.
 * 
 */
public class TermEnumIterator implements TermIterator {

	private ExceptionWrapper exceptionWrapper;
	private TermEnum enumeration;
	private Boolean hasNext;
	private Term next;
	private Term toTerm;
	private boolean toTermInclusive;

	public TermEnumIterator(TermEnum enumeration) {
		this.enumeration = enumeration;
	}
	
	public TermEnumIterator(TermEnum enumeration, Term toTerm) {
		this.enumeration = enumeration;
		this.toTerm = toTerm;
	}
	
	public TermEnumIterator(TermEnum enumeration, Term toTerm, boolean toTermInclusive) {
		this.enumeration = enumeration;
		this.toTerm = toTerm;
		this.toTermInclusive = toTermInclusive;
	}

	@Override
	public void skipTo(Term target) {
		try {
			enumeration.skipTo(target);
		} catch (IOException e) {
			throw exceptionWrapper.wrap(e);
		}
	}

	@Override
	public boolean hasNext() {
		boolean result;
		if (hasNext == null) {
			try {
				if (enumeration.next()) {
					next = enumeration.term();
					if (toTerm == null) {
						result = hasNext = true;
					} else {
						if (toTermInclusive) {
							result = hasNext = (next.compareTo(toTerm) <= 0);
						} else {
							result = hasNext = (next.compareTo(toTerm) < 0);
						}
					}
				} else {
					result = hasNext = false;
				}
			} catch (IOException e) {
				throw exceptionWrapper.wrap(e);
			}
		} else {
			result = hasNext;
		}
		return result;
	}

	@Override
	public Term next() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}
		hasNext = null;
		return next;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

}
