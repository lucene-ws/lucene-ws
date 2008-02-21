package net.lucenews3.http;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;

public interface SearchRequest {

	public String getText();
	
	public Query getQuery();
	
	public Analyzer getAnalyzer();
	
	public IndexSearcher getIndexSearcher();
	
}
