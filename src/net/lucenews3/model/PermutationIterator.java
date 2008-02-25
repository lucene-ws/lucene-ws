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

	private Object[][] valuesByIndex;
	private int[] positionsByIndex;
	private int highIndex;
	private List<T> next;
	
	@SuppressWarnings("unchecked")
	public PermutationIterator(Iterable<T>... iterables) {
		this.valuesByIndex = new Object[iterables.length][];
		this.positionsByIndex = new int[iterables.length];
		this.highIndex = iterables.length - 1;
		this.next = new ArrayList<T>(iterables.length);
		for (int i = 0; i < iterables.length; i++) {
			List<T> values = new ArrayList<T>();
			Iterator<T> iterator = iterables[i].iterator();
			while (iterator.hasNext()) {
				values.add(iterator.next());
			}
			this.valuesByIndex[i] = values.toArray(new Object[]{});
			this.positionsByIndex[i] = 0;
			this.next.add((T) this.valuesByIndex[i][0]);
		}
		this.positionsByIndex[0] = -1;
	}
	
	@SuppressWarnings("unchecked")
	public PermutationIterator(List<Iterable<T>> iterables) {
		this(iterables.toArray(new Iterable[]{}));
	}
	
	public boolean hasNext() {
		System.err.println("Is " + positionsByIndex[highIndex] + " less than " + valuesByIndex[highIndex].length);
		return positionsByIndex[highIndex] < valuesByIndex[highIndex].length;
	}
	
	public List<T> next() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}
		List<T> result = new ArrayList<T>();
		result.addAll(next);
		moveNext();
		return result;
	}
	
	protected void moveNext() {
		moveNext(0);
	}
	
	@SuppressWarnings("unchecked")
	protected void moveNext(int index) {
		try {
			System.err.println("Moving ahead index " + index);
			
			positionsByIndex[index]++;
			
			if (positionsByIndex[index] < valuesByIndex[index].length) {
				// Good
			} else {
				moveNext(index + 1);
				positionsByIndex[index] = 0;
			}
			
			next.set(index, (T) valuesByIndex[index][positionsByIndex[index]]);
		} catch (ArrayIndexOutOfBoundsException e) {
			// Uppermost index has increased, we are done
			System.err.println("Error, high position at " + positionsByIndex[highIndex]);
			e.printStackTrace();
		}
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
	
}
