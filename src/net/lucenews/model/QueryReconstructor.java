package net.lucenews.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;

public class QueryReconstructor {

	private int cursor;

	public QueryReconstructor() {
	}

	/**
	 * Reconstructs a query.
	 */

	public String reconstruct(Query query, String searchTerms) {
		StringBuffer buffer = new StringBuffer();
		cursor = 0;
		Logger.getLogger(this.getClass()).debug(
				"reconstructing '" + searchTerms + "'");
		reconstruct(query, searchTerms, buffer, "  ");
		buffer.append(searchTerms.substring(cursor, searchTerms.length()));
		return buffer.toString();
	}

	protected void reconstruct(Query query, String searchTerms,
			StringBuffer buffer) {
		reconstruct(query, searchTerms, buffer, "");
	}

	protected void reconstruct(Query query, String searchTerms,
			StringBuffer buffer, String indent) {
		Logger.getLogger(this.getClass()).debug(
				indent + "reconstructing Query: " + query + " (class: "
						+ query.getClass() + ", cursor:" + cursor + ")");

		if (query instanceof TokenTermQuery) {
			reconstruct((TokenTermQuery) query, searchTerms, buffer, indent
					+ "  ");
		} else if (query instanceof TokenBooleanQuery) {
			reconstruct((TokenBooleanQuery) query, searchTerms, buffer, indent
					+ "  ");
		} else if (query instanceof ExpandedTermQuery) {
			BooleanQuery booleanQuery = (BooleanQuery) query;
			Logger.getLogger(this.getClass()).debug(
					indent + "reconstructing ExpandedTermQuery: " + query);
			reconstruct(booleanQuery.getClauses()[0].getQuery(), searchTerms,
					buffer, indent + " ");
		} else if (query instanceof BooleanQuery) {
			reconstruct((BooleanQuery) query, searchTerms, buffer, indent
					+ "  ");
		}
	}

	protected void reconstruct(BooleanQuery booleanQuery, String searchTerms,
			StringBuffer buffer, String indent) {
		if (booleanQuery instanceof TokenBooleanQuery) {
			reconstruct((TokenBooleanQuery) booleanQuery, searchTerms, buffer,
					indent);
			return;
		}
		if (booleanQuery instanceof ExpandedTermQuery) {
			reconstruct((ExpandedTermQuery) booleanQuery, searchTerms, buffer,
					indent);
			return;
		}

		Logger.getLogger(this.getClass()).debug(
				indent + "reconstructing BooleanQuery: " + booleanQuery);
		BooleanClause[] clauses = booleanQuery.getClauses();
		for (int i = 0; i < clauses.length; i++) {
			reconstruct(clauses[i].getQuery(), searchTerms, buffer, indent
					+ "  ");
		}
	}

	protected void reconstruct(ExpandedTermQuery expandedTermQuery,
			String searchTerms, StringBuffer buffer, String indent) {
		Logger.getLogger(this.getClass()).debug(
				indent + "reconstructing ExpandedTermQuery: "
						+ expandedTermQuery);
	}

	protected void reconstruct(TokenTermQuery tokenTermQuery,
			String searchTerms, StringBuffer buffer, String indent) {
		int beginColumn = tokenTermQuery.getToken().beginColumn;
		int endColumn = tokenTermQuery.getToken().endColumn;

		Logger.getLogger(this.getClass()).debug(
				indent + "reconstructing TokenTermQuery: " + tokenTermQuery
						+ " (cursor:" + cursor + ", beginColumn: "
						+ beginColumn + ")");

		// append the gap
		buffer.append(searchTerms.substring(cursor, beginColumn));

		// append the text
		buffer.append(tokenTermQuery.getTerm().text());

		// update the cursor
		cursor = endColumn;
	}

	protected void reconstruct(TokenBooleanQuery tokenBooleanQuery,
			String searchTerms, StringBuffer buffer, String indent) {
		int beginColumn = tokenBooleanQuery.getToken().beginColumn;
		int endColumn = tokenBooleanQuery.getToken().endColumn;

		Logger.getLogger(this.getClass()).debug(
				indent + "reconstructing TokenBooleanQuery: "
						+ tokenBooleanQuery);

		// append the gap
		buffer.append(searchTerms.substring(cursor, beginColumn));

		// get the TokenTermQuery instances
		TokenTermQuery[] tokenTermQueries = getTokenTermQueries(
				tokenBooleanQuery).toArray(new TokenTermQuery[] {});

		// if there is one, do not append parenthesis
		if (tokenTermQueries.length == 1) {
			buffer.append(tokenTermQueries[0].getTerm().text());
		} else {
			buffer.append("(");
			boolean first = true;

			for (int i = 0; i < tokenTermQueries.length; i++) {
				TokenTermQuery termQuery = tokenTermQueries[i];
				if (first) {
					first = false;
				} else {
					buffer.append(" ");
				}
				buffer.append(termQuery.getTerm().text());
			}
			buffer.append(")");
		}

		// update the cursor
		cursor = endColumn;
	}

	public String reconstructTermQueries(Query alternate, String searchTerms) {
		StringBuffer reconstructed = new StringBuffer();

		Logger.getLogger(this.getClass()).debug(
				"Reconstructing query [" + alternate + "] using '"
						+ searchTerms + "'");

		List<TokenTermQuery> tokenTermQueries = getTokenTermQueries(alternate);
		Iterator<TokenTermQuery> tokenTermIterator = tokenTermQueries
				.iterator();

		int cursor = 0;
		while (tokenTermIterator.hasNext()) {
			TokenTermQuery tokenTermQuery = tokenTermIterator.next();
			Logger.getLogger(this.getClass()).debug(
					"tokenTermQuery " + tokenTermQuery + ", token: "
							+ tokenTermQuery.getToken());
			reconstructed.append(searchTerms.substring(cursor, tokenTermQuery
					.getToken().beginColumn));
			reconstructed.append(tokenTermQuery.getTerm().text());
			cursor = tokenTermQuery.getToken().endColumn;
		}

		reconstructed.append(searchTerms.substring(cursor));

		return reconstructed.toString();
	}

	/**
	 * For boolean queries
	 */

	public String reconstructBooleanQueries(Query alternate, String original) {
		StringBuffer reconstructed = new StringBuffer();

		List<TokenBooleanQuery> tokenBooleanQueries = getTokenBooleanQueries(alternate);
		Iterator<TokenBooleanQuery> tokenBooleanIterator = tokenBooleanQueries
				.iterator();

		int cursor = 0;
		while (tokenBooleanIterator.hasNext()) {
			TokenBooleanQuery tokenBooleanQuery = tokenBooleanIterator.next();
			int beginColumn = tokenBooleanQuery.getToken().beginColumn;
			int endColumn = tokenBooleanQuery.getToken().endColumn;

			List<TokenTermQuery> tokenTermQueries = getTokenTermQueries(tokenBooleanQuery);

			reconstructed.append(original.substring(cursor, beginColumn));

			if (tokenTermQueries.size() == 1) {
				reconstructed.append(tokenTermQueries.get(0).getTerm().text());
			} else {
				reconstructed.append("(");
				boolean first = true;

				Iterator<TokenTermQuery> tokenTermIterator = tokenTermQueries
						.iterator();
				while (tokenTermIterator.hasNext()) {
					TermQuery termQuery = tokenTermIterator.next();
					if (first) {
						first = false;
					} else {
						reconstructed.append(" ");
					}
					reconstructed.append(termQuery.getTerm().text());
				}
				reconstructed.append(")");
			}

			// rewritten.append( original.substring( cursor, beginColumn ) );
			// rewritten.append( query.getTerm().text() );
			cursor = endColumn;
		}

		reconstructed.append(original.substring(cursor));

		return reconstructed.toString();
	}

	/**
	 * Gets a list of all instances of TokenTermQuery within the context of this
	 * Query.
	 */

	public static List<TokenTermQuery> getTokenTermQueries(Query query) {
		List<TokenTermQuery> tokenTermQueries = new ArrayList<TokenTermQuery>();

		// BooleanQuery
		if (query instanceof BooleanQuery) {
			tokenTermQueries.addAll(getTokenTermQueries((BooleanQuery) query));
		}

		// TermQuery
		if (query instanceof TermQuery) {
			tokenTermQueries.addAll(getTokenTermQueries((TermQuery) query));
		}

		// sort according to beginning column
		Collections.sort(tokenTermQueries, new TokenTermQueryComparator());

		return tokenTermQueries;
	}

	/**
	 * Gets a list of all instances of TokenTermQuery within the context of this
	 * BooleanQuery.
	 */

	public static List<TokenTermQuery> getTokenTermQueries(
			BooleanQuery booleanQuery) {
		List<TokenTermQuery> tokenTermQueries = new ArrayList<TokenTermQuery>();

		BooleanClause[] clauses = booleanQuery.getClauses();
		for (int i = 0; i < clauses.length; i++) {
			tokenTermQueries.addAll(getTokenTermQueries(clauses[i].getQuery()));
		}

		return tokenTermQueries;
	}

	/**
	 * Gets a list of all instances of TokenTermQuery within the context of this
	 * TermQuery.
	 */

	public static List<TokenTermQuery> getTokenTermQueries(TermQuery termQuery) {
		List<TokenTermQuery> tokenTermQueries = new ArrayList<TokenTermQuery>();

		if (termQuery instanceof TokenTermQuery) {
			tokenTermQueries.add((TokenTermQuery) termQuery);
		}

		return tokenTermQueries;
	}

	/**
	 * Gets a list of all instances of TokenBooleanQuery within the context of
	 * this Query.
	 */

	public static List<TokenBooleanQuery> getTokenBooleanQueries(Query query) {
		List<TokenBooleanQuery> tokenBooleanQueries = new ArrayList<TokenBooleanQuery>();

		if (query instanceof BooleanQuery) {
			tokenBooleanQueries
					.addAll(getTokenBooleanQueries((BooleanQuery) query));
		}

		// sort according to beginning column
		Collections
				.sort(tokenBooleanQueries, new TokenBooleanQueryComparator());

		return tokenBooleanQueries;
	}

	/**
	 * Gets a list of all instances of TokenBooleanQuery within the context of
	 * this BooleanQuery.
	 */

	public static List<TokenBooleanQuery> getTokenBooleanQueries(
			BooleanQuery booleanQuery) {
		List<TokenBooleanQuery> tokenBooleanQueries = new ArrayList<TokenBooleanQuery>();

		if (booleanQuery instanceof TokenBooleanQuery) {
			tokenBooleanQueries.add((TokenBooleanQuery) booleanQuery);
		}

		BooleanClause[] clauses = booleanQuery.getClauses();
		for (int i = 0; i < clauses.length; i++) {
			tokenBooleanQueries.addAll(getTokenBooleanQueries(clauses[i]
					.getQuery()));
		}

		return tokenBooleanQueries;
	}

}

/**
 * Various comparators
 */

class TokenTermQueryComparator implements Comparator<TokenTermQuery> {

	public int compare(TokenTermQuery query1, TokenTermQuery query2) {
		Integer column1 = query1.getToken().beginColumn;
		Integer column2 = query2.getToken().beginColumn;
		return column1.compareTo(column2);
	}

}

class TokenBooleanQueryComparator implements Comparator<TokenBooleanQuery> {

	public int compare(TokenBooleanQuery query1, TokenBooleanQuery query2) {
		Integer column1 = query1.getToken().beginColumn;
		Integer column2 = query2.getToken().beginColumn;
		return column1.compareTo(column2);
	}

}
