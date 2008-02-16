package net.lucenews3.lucene.support;

public interface EqualityComparator<T> {

	public boolean isEqual(T value1, T value2);
	
}
