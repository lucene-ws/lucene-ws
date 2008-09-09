package net.lucenews3;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.FSDirectory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class FileSystemIndexRepository implements IndexRepository {

	private Logger logger;
	private Map<String, Index> indexesByName;
	private File directory;

	public FileSystemIndexRepository() {
		this.logger = Logger.getLogger(getClass());
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
		
		logger.debug("Looking for indexes in directory " + directory);
		
		File[] childDirectories = directory.listFiles();
		for (File childDirectory : childDirectories) {
			if (childDirectory.isDirectory()) {
				try {
					Index index = loadIndex(childDirectory);
					indexes.add(index);
				} catch (IOException e) {
					logger.error(e);
				}
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

	@SuppressWarnings("unchecked")
	public Index loadIndex(File directory) throws IOException {
		File springFile = new File(directory, "index.xml");
		
		if (!springFile.exists()) {
			throw new IOException("Unable to load index from directory " + directory + ", " + springFile + " not found");
		}
		
		ApplicationContext indexContext = new FileSystemXmlApplicationContext("file:" + springFile.getAbsolutePath());
		Map<String, ? extends Index> beansByName = (Map<String, ? extends Index>) indexContext.getBeansOfType(Index.class);
		
		int beanCount = beansByName.size();
		if (beanCount == 0) {
			throw new IOException("Unable to load index from " + springFile + ", no bean of type Index");
		} else if (beanCount > 1) {
			throw new IOException("Unable to load index from " + springFile + ", multiple beans of type Index");
		} else {
			return beansByName.values().iterator().next();
		}
	}

}
