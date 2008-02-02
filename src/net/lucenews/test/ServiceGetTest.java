package net.lucenews.test;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import net.lucenews.http.HttpResponse;

import org.apache.commons.httpclient.HttpStatus;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ServiceGetTest extends ClientTest {

	@Test
	public void testEmptyServiceByDirectory() throws Exception {
		doTestService("directory", new File[]{});
	}

	@Test
	public void testEmptyServiceByDirectories() throws Exception {
		doTestService("directories", new File[]{});
	}

	@Test
	public void testEmptyServiceByIndexDirectory() throws Exception {
		doTestService("index.directory", new File[]{});
	}

	@Test
	public void testEmptyServiceByIndexDirectories() throws Exception {
		doTestService("index.directories", new File[]{});
	}

	// TODO: @Test
	public void testEmptyServiceByIndicesDirectory() throws Exception {
		doTestService("indices.directory", new File[]{});
	}

	@Test
	public void testEmptyServiceByIndicesDirectories() throws Exception {
		doTestService("indices.directories", new File[]{});
	}

	@Test
	public void testSingleIndexServiceByDirectory() throws Exception {
		doTestService("directory", "testindex01");
	}

	@Test
	public void testSingleIndexServiceByDirectories() throws Exception {
		doTestService("directories", "testindex01");
	}

	@Test
	public void testSingleIndexerviceByIndexDirectory() throws Exception {
		doTestService("index.directory", "testindex01");
	}

	@Test
	public void testSingleIndexServiceByIndexDirectories() throws Exception {
		doTestService("index.directories", "testindex01");
	}

	// TODO: @Test
	public void testSingleIndexServiceByIndicesDirectory() throws Exception {
		doTestService("indices.directory", "testindex01");
	}

	@Test
	public void testSingleIndexServiceByIndicesDirectories() throws Exception {
		doTestService("indices.directories", "testindex01");
	}

	@Test
	public void testMultipleIndexServiceByDirectory() throws Exception {
		doTestService("directory", "testindex01", "testindex02", "testindex03");
	}

	@Test
	public void testMultipleIndexServiceByDirectories() throws Exception {
		doTestService("directories", "testindex01", "testindex02", "testindex03");
	}

	@Test
	public void testMultipleIndexerviceByIndexDirectory() throws Exception {
		doTestService("index.directory", "testindex01", "testindex02", "testindex03");
	}

	@Test
	public void testMultipleIndexServiceByIndexDirectories() throws Exception {
		doTestService("index.directories", "testindex01", "testindex02", "testindex03");
	}

	// TODO: @Test
	public void testMultipleIndexServiceByIndicesDirectory() throws Exception {
		doTestService("indices.directory", "testindex01", "testindex02", "testindex03");
	}

	@Test
	public void testMultipleIndexServiceByIndicesDirectories() throws Exception {
		doTestService("indices.directories", "testindex01", "testindex02", "testindex03");
	}

	public File[] getScatteredIndexes(final int count) {
		final File[] indexes = new File[count];
		for (int i = 0; i < count; i++) {
			final String indexName = "index" + string.padLeft(i + 1, '0', 2);
			final File index = new File(fileSystem.getTemporaryDirectory(), indexName);
			indexes[i] = index;
		}
		return indexes;
	}
	
	@Test
	public void testScatteredMultipleIndexServiceByDirectory() throws Exception {
		doTestService("directory", getScatteredIndexes(3));
	}

	@Test
	public void testScatteredMultipleIndexServiceByDirectories() throws Exception {
		doTestService("directories", getScatteredIndexes(3));
	}

	@Test
	public void testScatteredMultipleIndexerviceByIndexDirectory() throws Exception {
		doTestService("index.directory", getScatteredIndexes(3));
	}

	@Test
	public void testScatteredMultipleIndexServiceByIndexDirectories() throws Exception {
		doTestService("index.directories", getScatteredIndexes(3));
	}

	// TODO: @Test
	public void testScatteredMultipleIndexServiceByIndicesDirectory() throws Exception {
		doTestService("indices.directory", getScatteredIndexes(3));
	}

	@Test
	public void testScatteredMultipleIndexServiceByIndicesDirectories() throws Exception {
		doTestService("indices.directories", getScatteredIndexes(3));
	}
	
	protected void doTestService(String directoryParameterName, String... indexNames) throws Exception {
		File root = fileSystem.getTemporaryDirectory();
		File[] indexes = new File[indexNames.length];
		for (int i = 0; i < indexNames.length; i++) {
			String indexName = indexNames[i];
			File index = new File(root, indexName);
			indexes[i] = index;
		}
		doTestService(directoryParameterName, indexes);
	}
	
	protected void doTestService(final String directoryParameterName, final File... indexes) throws Exception {
		final Set<File> directories = new HashSet<File>();
		
		// Build indexes
		final String[] indexNames = new String[indexes.length];
		for (int i = 0; i < indexes.length; i++) {
			final File index = indexes[i];
			final File directory = index.getParentFile();
			final String indexName = index.getName();
			indexNames[i] = indexName;
			directories.add(directory);
			lucene.buildIndex(index);
		}
		
		final String directoryParameterValue = string.join(";", directories);
		
		if (logger.isLoggable(Level.FINER)) {
			logger.finer("directory parameter: " + directoryParameterValue);
		}
		
		// Register index's parent as the service's directory
		container.setInitialParameter(directoryParameterName, directoryParameterValue);
		
		// Request a list of indexes
		final HttpResponse response = http.sendRequest("http://localhost/lucene").getResponse();
		Document document = toDocument(response);
		
		// 200 OK
		Assert.assertEquals(HttpStatus.SC_OK, response.getStatus());
		
		// Verify introspection document
		introspectionDocumentAsserter.assertIntrospectionDocument(document);
		
		List<Element> workspaces = dom.elementsByPath(document, "/service/workspace");
		Assert.assertEquals(1, workspaces.size());
		
		Element workspace = workspaces.get(0);
		List<Element> collections = dom.elementsByPath(workspace, "./collection");
		Assert.assertEquals(indexNames.length, collections.size());
		
		for (int i = 0; i < indexNames.length; i++) {
			String indexName = indexNames[i];
			Element collection = collections.get(i);
			Assert.assertEquals("http://localhost/lucene/" + indexName + "/", collection.getAttribute("href"));
			// TODO String memberType = dom.innerText(dom.elementByPath(collection, "./member-type"));
			// TODO Assert.assertEquals("entry", memberType);
		}
	}
	
}
