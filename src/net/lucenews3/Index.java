package net.lucenews3;

import javax.servlet.http.HttpServletRequest;

import org.apache.lucene.document.Document;

public interface Index {

	public String getDisplayName();

	public Document getDocument(String key);

	public String addDocument(Document document);

	public String updateDocument(Document document);

	public Results search(HttpServletRequest req);

}
