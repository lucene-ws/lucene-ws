package net.lucenews3;

import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;

import org.apache.lucene.document.Document;

public interface DocumentIterator extends Iterator<Document> {

	/**
	 * Returns true if this iterator is iterating over a known collection.
	 * If the iterator is only iterating over a single element, this returns true.
	 * 
	 * @return
	 */
	public boolean isCollection();

	public String getTitle();

	public String getID();

	public String getSummary();

	public Date getUpdated() throws ParseException;

}
