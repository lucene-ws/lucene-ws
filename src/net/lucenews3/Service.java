package net.lucenews3;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.lucene.document.Document;

public interface Service {

	public List<Index> getIndexes() throws IOException;

	public String getIndexName(Index index);

	public Index getIndex(HttpServletRequest request) throws NoSuchIndexException, IOException;

	public DocumentIterator getDocumentIterator(HttpServletRequest request) throws IOException;

	public String getIndexURI(HttpServletRequest request, Index index);

	public String getDocumentURI(HttpServletRequest request, Index index, Document document);

	public void createIndex(String name) throws IOException;

}
