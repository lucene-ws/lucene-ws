package net.lucenews3.lucene.support;

import java.util.List;

import org.apache.lucene.search.Filter;
import org.apache.lucene.search.Query;

public interface DocumentList extends List<Document> {
	
	public DocumentList filteredBy(Filter filter);
	
	public ResultList searchBy(Query query);

}
