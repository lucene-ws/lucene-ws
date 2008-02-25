package net.lucenews3.model;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.springframework.beans.factory.InitializingBean;

/**
 * Describes an object which is capable of adding documents to an index.
 * If included in a Spring bean description file, will automatically 
 * build the index unless specified otherwise by setting its autoBuild
 * property to false.
 *
 */
public abstract class AbstractIndexBuilder implements Runnable, InitializingBean {
	
	private Logger logger;
	private Directory directory;
	private File directoryPath;
	private Analyzer analyzer;
	private boolean autoCreate;
	private boolean autoBuild;
	private boolean asynchronous;
	private Object result;

	public AbstractIndexBuilder() {
		this.logger = Logger.getLogger(getClass());
		this.autoBuild = true;
		this.analyzer = new StandardAnalyzer();
		this.autoCreate = true;
		this.asynchronous = false;
	}
	
	public boolean hasResult() {
		return result != null;
	}
	
	public int getResult() throws Exception {
		while (!hasResult()) {
			try {
				wait();
			} catch (InterruptedException e) {
				
			}
		}
		
		if (result instanceof Exception) {
			throw (Exception) result;
		} else if (result instanceof Integer) {
			return (Integer) result;
		} else {
			throw new RuntimeException("Invalid result: " + result);
		}
	}
	
	public boolean isSuccessful() {
		try {
			getResult();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public void run() {
		try {
			buildIndex();
		} catch (Exception e) {
			result = e;
			notifyAll();
		}
	}
	
	/**
	 * 
	 * @return the number of documents added to the index
	 * @throws IOException
	 */
	public abstract int buildIndex(IndexWriter writer) throws Exception;
	
	public int buildIndex() throws Exception {
		int result;
		final IndexWriter writer = new IndexWriter(directory, analyzer, autoCreate);
		result = buildIndex(writer);
		writer.close();
		
		return result;
	}

	public Directory getDirectory() {
		return directory;
	}

	public void setDirectory(Directory directory) {
		this.directory = directory;
	}

	public File getDirectoryPath() {
		return directoryPath;
	}

	public void setDirectoryPath(File directoryPath) {
		this.directoryPath = directoryPath;
	}

	public Analyzer getAnalyzer() {
		return analyzer;
	}

	public void setAnalyzer(Analyzer analyzer) {
		this.analyzer = analyzer;
	}

	public boolean isAutoCreate() {
		return autoCreate;
	}

	public void setAutoCreate(boolean autoCreate) {
		this.autoCreate = autoCreate;
	}

	public boolean isAutoBuild() {
		return autoBuild;
	}

	public void setAutoBuild(boolean autoBuild) {
		this.autoBuild = autoBuild;
	}

	public boolean isAsynchronous() {
		return asynchronous;
	}

	public void setAsynchronous(boolean asynchronous) {
		this.asynchronous = asynchronous;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (autoBuild) {
			if (directory != null && IndexReader.indexExists(directory)) {
				return;
			}
			
			if (directoryPath != null && IndexReader.indexExists(directoryPath)) {
				return;
			}
			
			if (logger.isInfoEnabled()) {
				logger.info("Auto-building index (asynchronous? " + asynchronous + ")");
			}
			
			if (asynchronous) {
				new Thread(this).start();
			} else {
				buildIndex();
			}
		}
	}

}
