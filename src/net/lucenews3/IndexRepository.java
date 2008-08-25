package net.lucenews3;

import java.io.IOException;
import java.util.List;

public interface IndexRepository {

	public List<Index> getIndexes() throws IOException;

	public Index getIndex(String key) throws NoSuchIndexException, IOException;

	public void putIndex(String key, Index index);

}
