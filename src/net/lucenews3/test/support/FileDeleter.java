package net.lucenews3.test.support;

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
		this.logger.setLevel(Level.FINER);
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
		if (file.exists()) {
			if (file.isDirectory()) {
				final File[] childFiles = file.listFiles();
				if (childFiles != null) {
					for (final File childFile : childFiles) {
						delete(childFile, maximumAttempts);
					}
				}
			}

			boolean deleted;
			int attempts = 0;
			do {
				deleted = file.delete();
				attempts++;
			} while (!deleted && attempts < maximumAttempts);

			if (!deleted && logger.isLoggable(Level.WARNING)) {
				logger.warning("Could not delete file \"" + file + "\" in " + attempts + " attempts");
			} else if (deleted && logger.isLoggable(Level.FINER)) {
				logger.finer("Deleted file \"" + file + "\"");
			}
		}
	}

}
