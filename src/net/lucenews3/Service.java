package net.lucenews3;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.lucene.document.Document;

public interface Service {

	public Index getIndex(HttpServletRequest request);

	public DocumentIterator getDocumentIterator(HttpServletRequest request) throws IOException;

	public String getIndexURI(HttpServletRequest request, Index index);

	public String getDocumentURI(HttpServletRequest request, Index index, Document document);

}
