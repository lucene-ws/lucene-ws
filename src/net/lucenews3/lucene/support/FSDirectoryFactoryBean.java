package net.lucenews3.lucene.support;

import java.io.File;

import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * Builds instances of <code>FSDirectory</code>.
 *
 */
public class FSDirectoryFactoryBean implements FactoryBean, InitializingBean {

	private File file;
	private String path;
	private LockFactory lockFactory;
	
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

	public LockFactory getLockFactory() {
		return lockFactory;
	}

	public void setLockFactory(LockFactory lockFactory) {
		this.lockFactory = lockFactory;
	}

	@Override
	public Object getObject() throws Exception {
		Object result = null;
		
		if (file != null) {
			if (lockFactory == null) {
				result = FSDirectory.getDirectory(file);
			} else {
				result = FSDirectory.getDirectory(file, lockFactory);
			}
		} else if (path != null) {
			if (lockFactory == null) {
				result = FSDirectory.getDirectory(path);
			} else {
				result = FSDirectory.getDirectory(path, lockFactory);
			}
		}
		
		return result;
	}

	@Override
	public Class<?> getObjectType() {
		return FSDirectory.class;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (file != null && path != null) {
			throw new Exception("Both \"file\" and \"path\" have been set on FSDirectoryFactoryBean");
		}
	}

}
