package net.lucenews3.model;

import java.util.List;
import java.util.Map;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.Query;

public interface DocumentList extends List<Document> {
	
	public Map<Term, DocumentList> byTerm();
	
	public DocumentList byTerm(Term term);
	
	public DocumentList filteredBy(Filter filter);
	
	public ResultList searchBy(Query query);
	
	public ResultList searchBy(SearchRequest searchRequest);

}
