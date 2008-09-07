package net.lucenews3;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.FSDirectory;

public class FileSystemIndexRepository implements IndexRepository {

	private Map<String, Index> indexesByName;
	private File directory;

	public FileSystemIndexRepository() {
		this.indexesByName = new HashMap<String, Index>();
	}

	public File getDirectory() {
		return directory;
	}

	public void setDirectory(File directory) {
		this.directory = directory;
	}

	@Override
	public List<Index> getIndexes() throws IOException {
		List<Index> indexes = new ArrayList<Index>();
		
		File[] childDirectories = directory.listFiles();
		for (File childDirectory : childDirectories) {
			if (childDirectory.isDirectory() && IndexReader.indexExists(childDirectory)) {
				DirectoryIndex index = new DirectoryIndex(FSDirectory.getDirectory(childDirectory));
				index.setName(childDirectory.getName());
				indexes.add(index);
			}
		}
		
		return indexes;
	}

	@Override
	public Index getIndex(String key) throws NoSuchIndexException, IOException {
		String[] names = key.split(",\\s*");
		if (names.length == 1) {
			String name = names[0];
			if (indexesByName.containsKey(name)) {
				return indexesByName.get(name);
			} else {
				File indexDirectory = new File(directory, name);
				if (IndexReader.indexExists(indexDirectory)) {
					DirectoryIndex index = new DirectoryIndex(FSDirectory.getDirectory(indexDirectory));
					index.setName(key);
					return index;
				} else {
					throw new NoSuchIndexException();
				}
			}
		} else {
			List<Index> indexes = new ArrayList<Index>(names.length);
			for (String name : names) {
				indexes.add(getIndex(name));
			}
			return new CompositeIndex(indexes);
		}
	}

	@Override
	public void putIndex(String key, Index index) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createIndex(String name) throws IOException {
		createIndex(name, (Properties) null);
	}

	@Override
	public void createIndex(String name, Properties properties) throws IOException {
		File indexDirectory = new File(directory, name);
		if (indexDirectory.exists()) {
			
		} else {
			indexDirectory.mkdir();
			new IndexWriter(indexDirectory, new StandardAnalyzer(), true).close();
		}
	}

}
