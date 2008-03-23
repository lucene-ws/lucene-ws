package net.lucenews3.queryParser;

import java.util.Vector;

import net.lucenews3.ExceptionTranslator;
import net.lucenews3.ExceptionTranslatorImpl;
import net.lucenews3.model.QueryParser;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryParser.CharStream;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParserTokenManager;
import org.apache.lucene.search.Query;

/**
 * Delegates all calls to the given delegate object. This class implements the
 * QueryParserInternals interface, thereby exposing the protected internal methods
 * of the original query parser. This exposing of methods allows the delegate objects
 * to invoke the original query parser's method via this object. If no delegate object
 * has been set for this delegating query parser, this object will behave identically
 * to its parent.
 *
 */
public class DelegatingQueryParser extends org.apache.lucene.queryParser.QueryParser
		implements QueryParser<String>, QueryParserInternals {

	private QueryParserDelegate delegate;
	private ExceptionTranslator exceptionTranslator;
	
	public DelegatingQueryParser(CharStream stream) {
		super(stream);
		this.delegate = new QueryParserDelegateAdaptor();
		this.exceptionTranslator = new ExceptionTranslatorImpl();
	}
	
	public DelegatingQueryParser(CharStream stream, QueryParserDelegate delegate) {
		super(stream);
		this.delegate = delegate;
		this.exceptionTranslator = new ExceptionTranslatorImpl();
	}

	public DelegatingQueryParser(QueryParserTokenManager tokenManager) {
		super(tokenManager);
		this.delegate = new QueryParserDelegateAdaptor();
		this.exceptionTranslator = new ExceptionTranslatorImpl();
	}

	public DelegatingQueryParser(QueryParserTokenManager tokenManager, QueryParserDelegate delegate) {
		super(tokenManager);
		this.delegate = delegate;
		this.exceptionTranslator = new ExceptionTranslatorImpl();
	}

	public DelegatingQueryParser(String field, Analyzer analyzer) {
		super(field, analyzer);
		this.delegate = new QueryParserDelegateAdaptor();
		this.exceptionTranslator = new ExceptionTranslatorImpl();
	}

	public DelegatingQueryParser(String field, Analyzer analyzer, QueryParserDelegate delegate) {
		super(field, analyzer);
		this.delegate = delegate;
		this.exceptionTranslator = new ExceptionTranslatorImpl();
	}
	
	public QueryParserDelegate getDelegate() {
		return delegate;
	}

	public void setDelegate(QueryParserDelegate delegate) {
		this.delegate = delegate;
	}

	public ExceptionTranslator getExceptionTranslator() {
		return exceptionTranslator;
	}

	public void setExceptionTranslator(ExceptionTranslator exceptionTranslator) {
		this.exceptionTranslator = exceptionTranslator;
	}

	/**
	 * Determines whether or not a method was called from
	 * a QueryParserDelegate object.
	 * @return
	 */
	protected boolean isCalledFromDelegate() {
		return isCalledFromDelegate(1);
	}

	protected boolean isCalledFromDelegate(int depth) {
		final Thread thread = Thread.currentThread();
		final StackTraceElement[] elements = thread.getStackTrace();
		for (int i = depth + 3; i < elements.length; i++) {
			final StackTraceElement element = elements[i];
			
			final Class<?> c;
			try {
				c = Class.forName(element.getClassName());
			} catch (ClassNotFoundException e) {
				throw exceptionTranslator.translate(e);
			}
			
			if (QueryParser.class.isAssignableFrom(c)) {
				return false;
			}
			
			if (QueryParserDelegate.class.isAssignableFrom(c)) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	public Query parse(String queryText) {
		final Query result;
		
		try {
			if (isCalledFromDelegate()) {
				result = super.parse(queryText);
			} else {
				result = delegate.parse(this, queryText);
			}
		} catch (ParseException e) {
			throw exceptionTranslator.translate(e);
		}
		
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Query getBooleanQuery(Vector clauses)
			throws ParseException {
		final Query result;
		
		if (delegate == null || isCalledFromDelegate()) {
			result = super.getBooleanQuery(clauses);
		} else {
			result = delegate.getBooleanQuery(this, clauses);
		}
		
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Query getBooleanQuery(Vector clauses, boolean disableCoord)
			throws ParseException {
		final Query result;
		
		if (delegate == null || isCalledFromDelegate()) {
			result = super.getBooleanQuery(clauses, disableCoord);
		} else {
			result = delegate.getBooleanQuery(this, clauses, disableCoord);
		}
		
		return result;
	}
	
	@Override
	public Query getFieldQuery(String field, String queryText)
			throws ParseException {
		final Query result;
		
		if (delegate == null || isCalledFromDelegate()) {
			result = super.getFieldQuery(field, queryText);
		} else {
			result = delegate.getFieldQuery(this, field, queryText);
		}
		
		return result;
	}
	
	@Override
	public Query getFieldQuery(String field, String queryText, int slop)
			throws ParseException {
		final Query result;
		
		if (delegate == null || isCalledFromDelegate()) {
			result = super.getFieldQuery(field, queryText, slop);
		} else {
			result = delegate.getFieldQuery(this, field, queryText, slop);
		}
		
		return result;
	}

	public Query getFuzzyTerm(String field, String termStr, float minSimilarity)
			throws ParseException {
		final Query result;
		
		if (delegate == null || isCalledFromDelegate()) {
			result = super.getFuzzyQuery(field, termStr, minSimilarity);
		} else {
			result = delegate.getFuzzyTerm(this, field, termStr, minSimilarity);
		}
		
		return result;
	}

	public Query getPrefixQuery(String field, String termStr)
			throws ParseException {
		final Query result;
		
		if (delegate == null || isCalledFromDelegate()) {
			result = super.getPrefixQuery(field, termStr);
		} else {
			result = delegate.getPrefixQuery(this, field, termStr);
		}
		
		return result;
	}

	@Override
	public Query getRangeQuery(String field, String part1, String part2, boolean inclusive)
			throws ParseException {
		final Query result;
		
		if (delegate == null || isCalledFromDelegate()) {
			result = super.getRangeQuery(field, part1, part2, inclusive);
		} else {
			result = delegate.getRangeQuery(this, field, part1, part2, inclusive);
		}
		
		return result;
	}

	@Override
	public Query getWildcardQuery(String field, String termStr)
			throws ParseException {
		final Query result;
		
		if (delegate == null || isCalledFromDelegate()) {
			result = super.getWildcardQuery(field, termStr);
		} else {
			result = delegate.getWildcardQuery(this, field, termStr);
		}
		
		return result;
	}

}
