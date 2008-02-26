package net.lucenews3.queryParser;

import net.lucenews3.model.Notes;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.Token;
import org.apache.lucene.search.Query;
import org.junit.Before;
import org.junit.Test;

public class TokenAnnotatingQueryParserDelegateTest {

	private DelegatingQueryParser parser;
	private QueryParserDelegate delegate;
	
	@Before
	public void setup() {
		this.parser = new DelegatingQueryParser("default", new StandardAnalyzer());
		TokenAnnotatingQueryParserDelegate textDelegate = new TokenAnnotatingQueryParserDelegate();
		LoggingQueryParserDelegate loggingDelegate = new LoggingQueryParserDelegate();
		this.delegate = textDelegate;
		textDelegate.target = loggingDelegate;
		//this.delegate = loggingDelegate;
		this.parser.setDelegate(this.delegate);
	}
	
	@Test
	public void testSingleTerm() {
		Query query = parser.parse("dog AND (cat OR bear)");
		Token token = Notes.get(query, "token", Token.class);
		System.out.println(token);
	}
	
}
