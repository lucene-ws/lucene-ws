package net.lucenews3.model;

import java.util.AbstractCollection;
import java.util.Iterator;
import java.util.List;

public class PermutationCollection<T> extends AbstractCollection<List<T>> {

	private Iterable<T>[] iterables;
	private Integer size;
	
	public PermutationCollection(Iterable<T>... iterables) {
		this.iterables = iterables;
	}
	
	@Override
	public Iterator<List<T>> iterator() {
		return new PermutationIterator<T>(iterables);
	}

	@Override
	public int size() {
		if (size == null) {
			if (iterables.length == 0) {
				size = 0;
			} else {
				size = 1;
				for (Iterable<T> iterable : iterables) {
					size *= size(iterable);
				}
			}
		}
		return size;
	}
	
	protected int size(Iterable<T> iterable) {
		int result = 0;
		Iterator<T> iterator = iterable.iterator();
		while (iterator.hasNext()) {
			iterator.next();
			result++;
		}
		return result;
	}

}
