package net.lucenews3.lucene.support;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Query;

public class QueryVisitorTest extends DefaultQueryVisitor {

	public static void main(String... arguments) throws Exception {
		QueryVisitorTest visitor = new QueryVisitorTest();
		
		String text = "(title:John AND author:Sam) OR date:[foo TO bar]";
		
		QueryParser parser = new QueryParser("all", new StandardAnalyzer());
		Query query = parser.parse(text);
		
		visitor.visit(query);
	}
	
}
