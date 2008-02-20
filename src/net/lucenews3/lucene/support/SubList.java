package net.lucenews3.lucene.support;

import java.util.AbstractList;
import java.util.List;

public class SubList<E> extends AbstractList<E> {
	
	private List<E> source;
	private int fromIndex;
	private int toIndex;
	
	public SubList(List<E> source, int fromIndex, int toIndex) {
		this.source = source;
		this.fromIndex = fromIndex;
		this.toIndex = toIndex;
	}

	private int toSourceIndex(int subIndex) {
		return fromIndex + subIndex;
	}
	
	@Override
	public E get(int index) {
		return source.get(toSourceIndex(index));
	}

	@Override
	public int size() {
		return Math.min(toIndex, source.size()) - fromIndex;
	}

}
