package net.lucenews3;

import org.apache.lucene.document.Document;

public interface Result {

	public String getTitle();

	public double getRelevance();

	public Document getDocument();

}
