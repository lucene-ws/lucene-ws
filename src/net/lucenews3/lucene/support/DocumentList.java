package net.lucenews3.lucene.support;

import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.Query;

public interface DocumentList extends List<Document> {
	
	public DocumentList where(Query criteria);

}
