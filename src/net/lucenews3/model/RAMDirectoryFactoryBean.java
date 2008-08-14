package net.lucenews3.model;

import java.io.File;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.springframework.beans.factory.FactoryBean;

public class RAMDirectoryFactoryBean implements FactoryBean {

	private Directory directory;
	private File file;
	private String path;
	
	public Directory getDirectory() {
		return directory;
	}

	public void setDirectory(Directory directory) {
		this.directory = directory;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public RAMDirectory getObject() throws Exception {
		RAMDirectory ramDirectory;
		
		if (directory != null) {
			ramDirectory = new RAMDirectory(directory);
		} else if (file != null) {
			ramDirectory = new RAMDirectory(file);
		} else if (path != null) {
			ramDirectory = new RAMDirectory(path);
		} else {
			ramDirectory = new RAMDirectory();
		}
		
		return ramDirectory;
	}

	@Override
	public Class<RAMDirectory> getObjectType() {
		return RAMDirectory.class;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}

}
