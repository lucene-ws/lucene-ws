package net.lucenews3;

public interface IndexRepository extends Iterable<Index> {

	public Index getIndex(String key) throws NoSuchIndexException;

	public void putIndex(String key, Index index);

}
