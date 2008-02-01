package net.lucenews.http;

import java.io.File;

public class DefaultIndexSource implements IndexSource {
	
	private File directory;
	
	public DefaultIndexSource() {
		
	}
	
	public DefaultIndexSource(File directory) {
		this.directory = directory;
	}

	public File getDirectory() {
		return directory;
	}

	public void setDirectory(File directory) {
		this.directory = directory;
	}

	@Override
	public Index getIndex(String indexName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IndexCollection getIndexes() {
		// TODO Auto-generated method stub
		return null;
	}

}
