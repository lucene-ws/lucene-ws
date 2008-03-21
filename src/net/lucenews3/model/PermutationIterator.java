package net.lucenews3.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Iterates over all possible permutations of the given collections.
 * For example, suppose you have the following two collections:
 * 	c1 = [A, B, C]
 *  c2 = [D, E, F]
 *  
 * new PermutationIterator(c1, c2);
 * 
 * The iterator would produce the following results:
 * 	[A, D]
 * 	[A, E]
 * 	[A, F]
 * 	[B, D]
 * 	[B, E]
 * 	[B, F]
 * 	[C, D]
 * 	[C, E]
 * 	[C, F]
 * 
 * @param <T>
 */
public class PermutationIterator<T> implements Iterator<List<T>> {

	private Iterable<T>[] iterables;
	private Iterator<T>[] iterators;
	private List<T> currentPermutation;
	private Boolean hasNext;
	
	@SuppressWarnings("unchecked")
	public static void main(String... arguments) throws Exception {
		List<String> a = new ArrayList<String>();
		a.add("Adam");
		a.add("Karen");
		
		List<String> b = new ArrayList<String>();
		b.add("Paynter");
		b.add("Buhler");
		
		PermutationIterator<String> i = new PermutationIterator<String>(a, b);
		
		while (i.hasNext()) {
			System.out.println(i.next());
		}
	}
	
	@SuppressWarnings("unchecked")
	public PermutationIterator(Iterable<T>... iterables) {
		this.iterables = iterables;
		
		// Prime the iterators
		this.hasNext = null;
		this.iterators = new Iterator[this.iterables.length];
		for (int i = 0; i < this.iterables.length; i++) {
			final Iterator<T> iterator = this.iterables[i].iterator();
			this.iterators[i] = iterator;
			if (iterator.hasNext()) {
				// Good!
			} else {
				this.hasNext = false;
				break;
			}
		}
		
		this.currentPermutation = new ArrayList<T>(this.iterables.length);
	}

	@Override
	public boolean hasNext() {
		if (hasNext == null) {
			hasNext = moveNext();
		}
		
		return hasNext;
	}

	@Override
	public List<T> next() {
		List<T> result;
		
		if (hasNext()) {
			result = currentPermutation;
			hasNext = null;
		} else {
			throw new NoSuchElementException();
		}
		
		return result;
	}
	
	/**
	 * Attempts to move the iterator on to the next permutation.
	 * 
	 * @return true on success, false otherwise
	 */
	public boolean moveNext() {
		return moveNext(0);
	}
	
	public boolean moveNext(int index) {
		boolean result;
		
		if (index == iterables.length) {
			result = false;
		} else if (currentPermutation.size() == index) {
			currentPermutation.add(iterators[index].next());
			moveNext(index + 1);
			result = true;
		} else {
			final Iterator<T> iterator = iterators[index];
			
			if (iterator.hasNext()) {
				currentPermutation.set(index, iterator.next());
				result = true;
			} else {
				// Wrap around with a new iterator
				iterators[index] = iterables[index].iterator();
				currentPermutation.set(index, iterators[index].next());
				
				// Move the next position's iterator ahead
				result = moveNext(index + 1);
			}
		}
		
		return result;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
	
}
