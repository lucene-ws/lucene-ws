package net.lucenews3.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermEnum;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.InitializingBean;

/**
 * Responsible for discovering indexes within the file system.
 *
 */
public class IndexDiscoverer implements Runnable, InitializingBean {
	
	private Map<IndexIdentity, Index> indexesByIdentity;
	private Set<File> visitedDirectories;
	private Logger logger;
	
	public IndexDiscoverer() {
		this.visitedDirectories = new HashSet<File>();
		this.logger = Logger.getLogger(getClass());
	}
	
	@Override
	public void run() {
		final File directory = new File(".");
		
		if (logger.isInfoEnabled()) {
			try {
				logger.info("Beginning search for indexes in " + directory.getCanonicalPath());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		try {
			search(directory);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		new Thread(this).start();
	}
	
	public void search(File directory) throws IOException {
		if (directory == null) {
			// Don't bother with this directory
		} else if (visitedDirectories.contains(directory.getCanonicalFile())) {
			// Don't bother with this directory
		} else if (directory.isDirectory()) {
			visitedDirectories.add(directory.getCanonicalFile());
			
			if (isIndex(directory) && isDiscoverable(directory)) {
				try {
					final IndexIdentity indexIdentity = buildIndexIdentity(directory);
					final Index index = buildIndex(directory);
					indexesByIdentity.put(indexIdentity, index);
				} catch (IOException e) {
					// TODO
					throw e;
				}
			} else {
				// Search children first
				File[] childFiles = directory.listFiles();
				for (File childFile : childFiles) {
					search(childFile);
				}
				
				// Search parent afterward
				File parentFile = directory.getParentFile();
				search(parentFile);
			}
		}
	}

	public boolean isIndex(File directory) {
		return IndexReader.indexExists(directory);
	}
	
	public boolean isDiscoverable(File directory) throws IOException {
		return !isDiscovered(directory);
		//return true;
	}
	
	public boolean isDiscovered(File directory) throws IOException {
		for (Index index : indexesByIdentity.values()) {
			if (index instanceof IndexImpl) {
				IndexImpl indexImpl = (IndexImpl) index;
				Directory luceneDirectory = indexImpl.getDirectory();
				if (luceneDirectory instanceof FSDirectory) {
					final FSDirectory luceneFSDirectory = (FSDirectory) luceneDirectory;
					if (directory.getCanonicalFile().equals(luceneFSDirectory.getFile().getCanonicalFile())) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public IndexIdentity buildIndexIdentity(File directory) {
		return new IndexIdentityImpl(directory.getName());
	}
	
	public Index buildIndex(File directory) throws IOException {
		IndexImpl index = new IndexImpl();
		
		index.setDirectory(FSDirectory.getDirectory(directory));
		index.setMetaData(buildIndexMetaData(directory));
		
		return index;
	}
	
	public IndexMetaData buildIndexMetaData(File directory) throws IOException {
		IndexMetaDataImpl metaData = new IndexMetaDataImpl();
		metaData.setName(directory.getCanonicalPath());
		
		final IndexReader reader = IndexReader.open(directory);
		try {
			int documentCount = reader.numDocs();
			System.err.println(directory + ": document count = " + documentCount);
			TermEnum terms = reader.terms();
			List<String> fieldNames = new ArrayList<String>();
			String primaryFieldName = null;
			String currentFieldName = null;
			int currentFieldCount = 0;
			while (terms.next()) {
				Term term = terms.term();
				String fieldName = term.field();
				
				if (currentFieldName == null) {
					currentFieldName = fieldName;
					currentFieldCount = 1;
					fieldNames.add(currentFieldName);
				} else {
					currentFieldCount++;
					if (currentFieldName.equals(fieldName)) {
						// Nothing to see here
					} else {
						System.err.println(directory + ": field \"" + currentFieldName + "\" appears in " + currentFieldCount);
						// Switching over to the next field
						if (primaryFieldName == null && currentFieldCount == documentCount) {
							primaryFieldName = currentFieldName;
						}
						currentFieldName = fieldName;
						currentFieldCount = 1;
						fieldNames.add(currentFieldName);
					}
				}
			}

			System.err.println(directory + ": field \"" + currentFieldName + "\" appears in " + currentFieldCount);
			
			if (primaryFieldName == null && currentFieldCount == documentCount) {
				primaryFieldName = currentFieldName;
			}
			
			if (primaryFieldName == null) {
				// Well, we tried
				metaData.setPrimaryField(primaryFieldName);
			} else {
				metaData.setPrimaryField(primaryFieldName);
			}
			metaData.setDefaultFields(fieldNames);
		} finally {
			reader.close();
		}
		
		return metaData;
	}

	public Map<IndexIdentity, Index> getIndexesByIdentity() {
		return indexesByIdentity;
	}

	public void setIndexesByIdentity(Map<IndexIdentity, Index> indexesByIdentity) {
		this.indexesByIdentity = indexesByIdentity;
	}
	
}
