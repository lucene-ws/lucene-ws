package net.lucenews3;

import java.util.Date;

import org.apache.lucene.document.Document;

public interface DocumentMetaData {

	public Index getIndex();

	public Document getDocument();

	public String getIdentity();

	public Date getLastUpdated();

}
