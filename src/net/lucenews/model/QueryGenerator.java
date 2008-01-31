package net.lucenews.model;

import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;

public class QueryGenerator {

	private String defaultField;

	private QueryParser.Operator defaultOperator;

	public QueryGenerator() {
		defaultOperator = QueryParser.Operator.OR;
	}

	public String getDefaultField() {
		return defaultField;
	}

	public void setDefaultField(String defaultField) {
		this.defaultField = defaultField;
	}

	public QueryParser.Operator getDefaultOperator() {
		return defaultOperator;
	}

	public void setDefaultOperator(QueryParser.Operator defaultOperator) {
		this.defaultOperator = defaultOperator;
	}

	/**
	 * Generates a human-readable String representation of the given query.
	 * Meant as a better alternative to {@link Query#toString Query.toString}.
	 * 
	 * @param query
	 *            the query to translate
	 * @return a String representation of the given query
	 */

	public String generate(Query query) {
		GenerationReport r = report(query);
		if (r == null)
			return query.toString(getDefaultField());
		return r.getGenerated();
	}

	protected GenerationReport report(Query query) {
		if (query instanceof TermQuery) {
			return report((TermQuery) query);
		} else if (query instanceof BooleanQuery) {
			return report((BooleanQuery) query);
		} else {
			return new GenerationReport(query.toString(defaultField));
		}
	}

	protected GenerationReport report(TermQuery query) {
		String field = query.getTerm().field();
		String text = query.getTerm().text().trim();

		String asString = null;
		if (text.contains(" ")) {
			asString = "\"" + text + "\"";
		} else {
			if (text.startsWith("\""))
				text = text.substring(1);
			if (text.endsWith("\""))
				text = text.substring(0, text.length() - 2);
			asString = text;
		}

		if (!field.equals(defaultField))
			asString = field + ":" + asString;

		return new GenerationReport(asString);
	}

	protected GenerationReport report(BooleanQuery query) {
		BooleanClause[] clauses = query.getClauses();
		StringBuffer buffer = new StringBuffer();

		for (int i = 0; i < clauses.length; i++) {
			BooleanClause clause = clauses[i];

			boolean parenthesisRequired = false;

			if (i > 0) {
				if (defaultOperator == QueryParser.Operator.AND
						|| clause.isRequired()) {
					buffer.append(" AND ");
				} else if (defaultOperator == QueryParser.Operator.OR) {
					buffer.append(" OR ");
				}
			}

			if (clause.isProhibited()) {
				buffer.append("NOT ");
				parenthesisRequired = true;
			}

			/**
			 * Sub report
			 */

			GenerationReport subReport = report(clause.getQuery());
			if (parenthesisRequired && subReport.parenthesisRequired())
				buffer.append("(" + subReport + ")");
			else
				buffer.append(subReport);

			if (subReport != null)
				parenthesisRequired = parenthesisRequired
						|| subReport.parenthesisRequired();

			GenerationReport rr = new GenerationReport(buffer.toString());
			rr.setParenthesisRequired(parenthesisRequired);
		}

		return new GenerationReport(buffer.toString());
	}

}

class GenerationReport {

	private String generated;

	private boolean parenthesisRequired;

	public GenerationReport(String generated) {
		this.generated = generated;
	}

	public String getGenerated() {
		return generated;
	}

	public boolean parenthesisRequired() {
		return parenthesisRequired;
	}

	public void setParenthesisRequired(boolean parenthesisRequired) {
		this.parenthesisRequired = parenthesisRequired;
	}

	public String toString() {
		return getGenerated();
	}
}
