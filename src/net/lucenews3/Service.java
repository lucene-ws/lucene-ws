package net.lucenews3;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.lucene.document.Document;

public interface Service {

	public List<Index> getIndexes() throws IOException;

	public String getIndexName(Index index);

	public Index getIndex(HttpServletRequest request) throws NoSuchIndexException, IOException;

	public DocumentIterator getDocumentIterator(HttpServletRequest request) throws IOException;

	public URI getIndexURI(HttpServletRequest request, Index index) throws URISyntaxException;

	public URI getDocumentURI(HttpServletRequest request, Index index, Document document) throws URISyntaxException, IOException;

	public void createIndex(String name) throws IOException;

}
