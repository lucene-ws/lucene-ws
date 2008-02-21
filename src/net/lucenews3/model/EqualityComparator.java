package net.lucenews3.model;

public interface EqualityComparator<T> {

	public boolean isEqual(T value1, T value2);
	
}
