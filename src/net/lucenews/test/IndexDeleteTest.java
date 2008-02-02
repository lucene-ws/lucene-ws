package net.lucenews.test;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.apache.lucene.index.IndexReader;
import org.junit.Assert;
import org.junit.Test;

public class IndexDeleteTest extends ClientTest {

	@Test
	public void testIndexDeletion() throws Exception {
		String indexName = client.getRandomIndexName();
		doTestDeletion(indexName);
	}
	
	@Test
	public void testIndexesDeletion() throws Exception {
		doTestDeletion(client.getRandomIndexName(), client.getRandomIndexName());
	}
	
	@Test
	public void testIndexInvalidation() throws Exception {
		String indexName = client.getRandomIndexName();
		doTestInvalidation(indexName);
	}
	
	@Test
	public void testIndexesInvalidation() throws Exception {
		doTestInvalidation(client.getRandomIndexName(), client.getRandomIndexName());
	}
	
	public void doTestInvalidation(String... indexNames) throws Exception {
		File root = fileSystem.getTemporaryDirectory();
		
		File[] indexes = new File[indexNames.length];
		for (int i = 0; i < indexes.length; i++) {
			String indexName = indexNames[i];
			File index = new File(root, indexName);
			indexes[i] = index;
		}
		
		doTestInvalidation(indexes);
	}
	
	public void doTestInvalidation(File... indexes) throws Exception {
		Set<File> directories = new HashSet<File>();
		
		for (File index : indexes) {
			directories.add(index.getParentFile());
			lucene.buildIndex(index);
		}
		
		File root = fileSystem.getTemporaryDirectory();
		container.setInitialParameter("directory", string.join(";", directories));
		
		for (File index : indexes) {
			String indexName = index.getName();
			client.deleteIndex(indexName);
			Assert.assertFalse("\"" + index + "\" contains no Lucene index", IndexReader.indexExists(new File(root, indexName)));
		}
	}
	
	public void doTestDeletion(String... indexNames) throws Exception {
		File root = fileSystem.getTemporaryDirectory();
		
		File[] indexes = new File[indexNames.length];
		for (int i = 0; i < indexes.length; i++) {
			String indexName = indexNames[i];
			File index = new File(root, indexName);
			indexes[i] = index;
		}
		
		doTestDeletion(indexes);
	}
	
	public void doTestDeletion(File... indexes) throws Exception {
		Set<File> directories = new HashSet<File>();
		
		for (File index : indexes) {
			directories.add(index.getParentFile());
			lucene.buildIndex(index);
		}
		
		File root = fileSystem.getTemporaryDirectory();
		container.setInitialParameter("directory", string.join(";", directories));
		
		for (File index : indexes) {
			String indexName = index.getName();
			client.deleteIndex(indexName);
			Assert.assertFalse("\"" + index + "\" deleted", new File(root, indexName).exists());
		}
	}
	
}
