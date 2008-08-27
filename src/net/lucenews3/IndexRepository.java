package net.lucenews3;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

public interface IndexRepository {

	public List<Index> getIndexes() throws IOException;

	public Index getIndex(String key) throws NoSuchIndexException, IOException;

	public void putIndex(String key, Index index);

	void createIndex(String name) throws IOException;

	void createIndex(String name, Properties properties) throws IOException;

}
