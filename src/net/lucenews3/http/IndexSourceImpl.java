package net.lucenews3.http;

import java.io.File;

public class IndexSourceImpl implements IndexSource {
	
	private File directory;
	
	public IndexSourceImpl() {
		
	}
	
	public IndexSourceImpl(File directory) {
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
