package net.lucenews3.queryParser;

import java.util.List;

import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;

public class MultiFieldQueryParserDelegate extends QueryParserDelegateAdaptor {

	private List<String> defaultFields;
	
	public MultiFieldQueryParserDelegate() {
		super();
	}

	public MultiFieldQueryParserDelegate(QueryParserDelegate target) {
		super(target);
	}

	public List<String> getDefaultFields() {
		return defaultFields;
	}

	public void setDefaultFields(List<String> defaultFields) {
		this.defaultFields = defaultFields;
	}

	@Override
	public Query getFieldQuery(QueryParserInternals parser, String field,
			String queryText, int slop) throws ParseException {
		final Query result;
		
		if (isImplicitField()) {
			final BooleanQuery booleanQuery = new BooleanQuery();
			
			for (String defaultField : defaultFields) {
				booleanQuery.add(super.getFieldQuery(parser, defaultField, queryText, slop), BooleanClause.Occur.SHOULD);
			}
			
			result = booleanQuery;
		} else {
			result = super.getFieldQuery(parser, field, queryText, slop);
		}
		
		return result;
	}
	
	public boolean isImplicitField() {
		// TODO Implement isImplicitField()
		return false;
	}
	
}
