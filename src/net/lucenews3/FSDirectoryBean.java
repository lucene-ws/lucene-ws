package net.lucenews3;

import java.io.File;

import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockFactory;
import org.springframework.beans.factory.FactoryBean;

public class FSDirectoryBean implements FactoryBean {

	private File file;
	private String path;
	private LockFactory lockFactory;

	@Override
	public Object getObject() throws Exception {
		// TODO Auto-generated method stub
		if (file == null) {
			if (path == null) {
				if (lockFactory == null) {
					
				} else {
					
				}
			} else {
				if (lockFactory == null) {
					return FSDirectory.getDirectory(path);
				} else {
					return FSDirectory.getDirectory(path, lockFactory);
				}
			}
		} else {
			if (path == null) {
				if (lockFactory == null) {
					return FSDirectory.getDirectory(file);
				} else {
					return FSDirectory.getDirectory(file, lockFactory);
				}
			} else {
				if (lockFactory == null) {
					
				} else {
					
				}
			}
		}
		return null;
	}

	@Override
	public Class<FSDirectory> getObjectType() {
		return FSDirectory.class;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}

}
