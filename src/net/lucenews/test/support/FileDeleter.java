package net.lucenews.test.support;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileDeleter implements Runnable {

	private Iterable<File> files;
	private int defaultMaximumAttempts;
	private Logger logger;
	
	public FileDeleter(Iterable<File> files) {
		this.files = files;
		this.logger = Logger.getLogger("net.lucenews.test.support");
	}
	
	public void run() {
		for (File file : files) {
			delete(file);
		}
	}
	
	public void delete(final File file) {
		delete(file, defaultMaximumAttempts);
	}
	
	public void delete(final File file, final int maximumAttempts) {
		if (file.isDirectory()) {
			for (File child : file.listFiles()) {
				delete(child);
			}
		}
		
		boolean deleted;
		int attempts = 0;
		do {
			deleted = file.delete();
			attempts++;
		} while (!deleted && attempts < maximumAttempts);
		
		if (!deleted && logger.isLoggable(Level.WARNING)) {
			logger.warning("Could not delete file \"" + file + "\"");
		}
	}
	
}
