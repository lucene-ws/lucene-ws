package net.lucenews3.model;

import java.io.File;
import java.util.concurrent.BlockingQueue;

import org.apache.lucene.index.IndexReader;
import org.springframework.beans.factory.InitializingBean;

public class IndexDiscoverer implements Runnable, InitializingBean {
	
	private BlockingQueue<Runnable> queue;
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	public void search(File directory) {
		if (isIndex(directory)) {
			Runnable registrar = getIndexRegistrar(directory);
			
			boolean isAdded = false;
			while (!isAdded) {
				try {
					queue.put(registrar);
					isAdded = true;
				} catch (InterruptedException e) {
					isAdded = false;
				}
			}
			
			
		}
	}

	public boolean isIndex(File directory) {
		return IndexReader.indexExists(directory);
	}
	
	public Runnable getIndexRegistrar(File directory) {
		return null;
	}
	
}
