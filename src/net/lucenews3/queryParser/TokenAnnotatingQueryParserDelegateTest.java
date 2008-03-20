package net.lucenews3.queryParser;

import net.lucenews3.model.TokenSource;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.Token;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.junit.Assert;
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
		String input = "dog AND (cat OR animal:bear)";
		Query query = parser.parse(input);
		Assert.assertTrue(query instanceof TokenSource);
		TokenSource tokenSource = (TokenSource) query;
		Token token = tokenSource.getToken();
		Assert.assertEquals(input, token.image);
		
		Assert.assertTrue(query instanceof BooleanQuery);
		BooleanQuery andQuery = (BooleanQuery) query;
		BooleanClause[] andClauses = andQuery.getClauses();
		
		Assert.assertEquals(2, andClauses.length);
		
		Query dogQuery = andClauses[0].getQuery();
		Assert.assertTrue(dogQuery instanceof TermQuery);
		Assert.assertTrue(dogQuery instanceof TokenSource);
		TermQuery dogTermQuery = (TermQuery) dogQuery;
		TokenSource dogTokenSource = (TokenSource) dogQuery;
		Token dogToken = dogTokenSource.getToken();
		Assert.assertEquals("dog", dogToken.image);
		Assert.assertEquals("dog", dogTermQuery.getTerm().text());
		
		BooleanQuery orQuery = (BooleanQuery) andClauses[1].getQuery();
		BooleanClause[] orClauses = orQuery.getClauses();
		Assert.assertEquals(2, orClauses.length);
		
		Query bearQuery = orClauses[1].getQuery();
		Assert.assertTrue(bearQuery instanceof TermQuery);
		TokenSource bearTokenSource = (TokenSource) bearQuery;
		Assert.assertNotNull(bearTokenSource.getToken());
		Assert.assertEquals("animal:bear", bearTokenSource.getToken().image);
	}
	
}
