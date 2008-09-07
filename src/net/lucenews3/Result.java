package net.lucenews3;

import org.apache.lucene.document.Document;

public interface Result {

	public int getNumber();

	public String getTitle();

	public double getScore();

	public Document getDocument();

	public DocumentMetaData getDocumentMetaData();

}
