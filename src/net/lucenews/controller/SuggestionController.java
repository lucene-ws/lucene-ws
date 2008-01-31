package net.lucenews.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.lucenews.LuceneContext;
import net.lucenews.LuceneUtils;
import net.lucenews.model.DidYouMeanQueryGenerator;
import net.lucenews.model.LuceneIndex;
import net.lucenews.model.LuceneSpellChecker;
import net.lucenews.model.LuceneSynonymExpander;
import net.lucenews.model.QueryReconstructor;
import net.lucenews.model.exception.IndicesNotFoundException;
import net.lucenews.opensearch.OpenSearchQuery;
import net.lucenews.opensearch.OpenSearchResponse;

import org.apache.log4j.Logger;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiReader;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.MultiSearcher;
import org.apache.lucene.search.Query;

public class SuggestionController extends Controller {

	public static void doSuggest(LuceneContext c, Query query,
			OpenSearchResponse response) throws IndicesNotFoundException,
			IOException {
		response.addQueries(getOpenSearchQuerySuggestions(c, query, c
				.getOpenSearchQuery().getSearchTerms()));
	}

	/**
	 * Retrieves a list of suggested OpenSearch Query objects
	 */

	public static List<OpenSearchQuery> getOpenSearchQuerySuggestions(
			LuceneContext c, Query original, String searchTerms)
			throws IndicesNotFoundException, IOException {
		MultiSearcher searcher = c.getMultiSearcher();

		// Query suggestions
		List<Query> suggestions = getSuggestions(c, original);

		// OpenSearch Query suggestions
		List<OpenSearchQuery> openSearchQueries = new ArrayList<OpenSearchQuery>();

		// Query Reconstructor
		QueryReconstructor reconstructor = c.getQueryReconstructor();

		// iterate
		Iterator<Query> iterator = suggestions.iterator();
		while (iterator.hasNext()) {
			Query query = iterator.next();

			// OpenSearch Query
			OpenSearchQuery openSearchQuery = new OpenSearchQuery();
			openSearchQuery.setRole(OpenSearchQuery.Role.CORRECTION);

			if (reconstructor == null || searchTerms == null) {
				openSearchQuery.setSearchTerms(query.toString());
			} else {
				openSearchQuery.setSearchTerms(reconstructor.reconstruct(query,
						searchTerms));
			}

			openSearchQuery.setTotalResults(searcher.search(query,
					c.getFilter()).length());

			openSearchQueries.add(openSearchQuery);
		}

		// sort according to descending totalResults
		Collections.sort(openSearchQueries, new OpenSearchQueryComparator());

		return openSearchQueries;
	}

	/**
	 * Retrieves a list of suggested Query objects.
	 */

	public static List<Query> getSuggestions(LuceneContext c, Query original)
			throws IndicesNotFoundException, IOException {
		List<Query> suggestions = new LinkedList<Query>();

		// similar
		if (c.suggestSimilar() != null && c.suggestSimilar()) {
			suggestions.addAll(suggestSimilar(c, original));
		}

		// spelling
		if (c.suggestSpelling() != null && c.suggestSpelling()) {
			suggestions.addAll(suggestSpelling(c, original));
		}

		// synonyms
		if (c.suggestSynonyms() != null && c.suggestSynonyms()) {
			suggestions.addAll(suggestSynonyms(c, original));
		}

		return suggestions;
	}

	/**
	 * Suggests similar queries
	 */

	public static List<Query> suggestSimilar(LuceneContext c, Query original)
			throws IndicesNotFoundException, IOException {
		List<Query> similar = new LinkedList<Query>();

		LuceneIndex[] indices = c.getService().getIndexManager().getIndices(
				c.getRequest().getIndexNames());

		IndexReader[] readers = new IndexReader[indices.length];

		for (int i = 0; i < indices.length; i++) {
			readers[i] = indices[i].getIndexReader();
		}

		MultiReader reader = new MultiReader(readers);

		try {
			DidYouMeanQueryGenerator generator = new DidYouMeanQueryGenerator(
					original, reader);
			similar.addAll(generator.getQuerySuggestions(true, true));

			Iterator<Query> iterator = similar.iterator();
			while (iterator.hasNext()) {
				setDefaultBoost(iterator.next());
			}
		} catch (Error err) {
		} catch (Exception e) {
		}

		for (int i = 0; i < indices.length; i++) {
			indices[i].putIndexReader(readers[i]);
		}

		return similar;
	}

	/**
	 * Suggests spell-checked queries
	 */

	public static List<Query> suggestSpelling(LuceneContext c, Query original)
			throws IOException {
		List<Query> spelling = new LinkedList<Query>();

		LuceneSpellChecker spellChecker = c.getSpellChecker();

		if (spellChecker != null) {
			spelling.addAll(spellChecker.suggestSimilar(original));
		}

		return spelling;
	}

	/**
	 * Suggests synonyms
	 */

	public static List<Query> suggestSynonyms(LuceneContext c, Query original)
			throws IOException {
		List<Query> synonyms = new LinkedList<Query>();

		LuceneSynonymExpander synonymExpander = c.getSynonymExpander();

		if (synonymExpander != null) {
			Logger.getLogger(SuggestionController.class).trace(
					"suggestSynonyms");

			Query clone = LuceneUtils.clone(original);
			Query synonymsQuery = synonymExpander.expand(clone);

			synonyms.add(synonymsQuery);
		}

		return synonyms;
	}

	public static void setDefaultBoost(Query query) {
		if (query instanceof BooleanQuery) {
			setDefaultBoost((BooleanQuery) query);
		} else {
			query.setBoost(1.0f);
		}
	}

	public static void setDefaultBoost(BooleanQuery query) {
		BooleanClause[] clauses = query.getClauses();
		for (int i = 0; i < clauses.length; i++) {
			setDefaultBoost(clauses[i].getQuery());
		}
		query.setBoost(1.0f);
	}
}

/**
 * A class used to assist sorting of OpenSearchQuery objects according to their
 * respective totalResults values.
 */

class OpenSearchQueryComparator implements Comparator<OpenSearchQuery> {

	public int compare(OpenSearchQuery q1, OpenSearchQuery q2) {
		Integer count1 = q1.getTotalResults();
		Integer count2 = q2.getTotalResults();

		if (count1 == null && count2 == null) {
			return 0;
		}

		if (count1 == null) {
			return -1;
		}

		if (count2 == null) {
			return 1;
		}

		return count2.compareTo(count1);
	}

}
