package net.lucenews.test.support;

import java.io.File;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Random;

public class FileSystemUtility {

	private File temporaryRoot;
	private Random random;
	private Deque<File> deletionQueue;
	private FileDeleter deleter;
	private boolean defaultAutoCreate;
	private boolean defaultAutoDelete;
	
	public FileSystemUtility() {
		this(new File("./temp"));
	}
	
	public FileSystemUtility(File temporaryRoot) {
		this.temporaryRoot = temporaryRoot;
		this.deletionQueue = new ArrayDeque<File>();
		this.deleter = new FileDeleter(deletionQueue);
		this.random = new Random();
		this.defaultAutoCreate = true;
		this.defaultAutoDelete = true;
		registerShutdownHook();
	}
	
	public String getTemporaryDirectoryName() {
		return "tempdir-" + random.nextInt();
	}
	
	public File getTemporaryDirectory() {
		return getTemporaryDirectory(defaultAutoCreate, defaultAutoDelete);
	}
	
	public File getTemporaryDirectory(final boolean autoCreate) {
		return getTemporaryDirectory(autoCreate, defaultAutoDelete);
	}
	
	public File getTemporaryDirectory(final boolean autoCreate, final boolean autoDelete) {
		String directoryName = getTemporaryDirectoryName();
		File directory = new File(temporaryRoot, directoryName);
		if (autoCreate && !directory.exists()) {
			directory.mkdirs();
		}
		if (autoDelete) {
			queueForDeletion(directory);
		}
		return directory;
	}
	
	public void registerShutdownHook() {
		registerShutdownHook(Runtime.getRuntime());
	}
	
	public void registerShutdownHook(final Runtime runtime) {
		runtime.addShutdownHook(new Thread(deleter));
	}
	
	public void queueForDeletion(final File file) {
		deletionQueue.add(file);
	}
	
}
