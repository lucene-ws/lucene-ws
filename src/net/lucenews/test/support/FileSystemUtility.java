package net.lucenews.test.support;

import java.io.File;
import java.util.Random;

public class FileSystemUtility {

	private File temporaryRoot;
	private Random random;
	
	public FileSystemUtility() {
		random = new Random();
		temporaryRoot = new File("./temp");
	}
	
	public String getTemporaryDirectoryName() {
		return "tempdir-" + random.nextInt();
	}
	
	public File getTemporaryDirectory() {
		return getTemporaryDirectory(true);
	}
	
	public File getTemporaryDirectory(boolean autoCreate) {
		String tempDirName = getTemporaryDirectoryName();
		File tempDir = new File(temporaryRoot, tempDirName);
		if (!tempDir.exists() && autoCreate) {
			tempDir.mkdirs();
		}
		return tempDir;
	}
	
}
