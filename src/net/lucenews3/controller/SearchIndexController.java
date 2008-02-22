package net.lucenews3.controller;

import java.util.List;
import java.util.Map;

import net.lucenews3.exception.NoSuchIndexException;
import net.lucenews3.model.Document;
import net.lucenews3.model.FieldList;
import net.lucenews3.model.Index;
import net.lucenews3.model.IndexIdentity;
import net.lucenews3.model.IndexIdentityParser;
import net.lucenews3.model.IndexRange;
import net.lucenews3.model.IndexRangeParser;
import net.lucenews3.model.Result;
import net.lucenews3.model.ResultList;
import net.lucenews3.model.SearchRequest;
import net.lucenews3.model.SearchRequestParser;
import net.lucenews3.opensearch.Response;
import net.lucenews3.opensearch.ResponseImpl;

import org.springframework.web.servlet.ModelAndView;

public class SearchIndexController<I, O> implements Controller<I, O> {
	
	private IndexIdentityParser<I> indexIdentityParser;
	private SearchRequestParser<I> searchRequestParser;
	private IndexRangeParser<I> indexRangeParser;
	private Map<IndexIdentity, Index> indexesByIdentity;

	@Override
	public ModelAndView handleRequest(I input, O output) throws Exception {
		final IndexIdentity indexIdentity = indexIdentityParser.parse(input);
		final Index index = indexesByIdentity.get(indexIdentity);
		
		if (index == null) {
			throw new NoSuchIndexException(indexIdentity);
		}
		
		final SearchRequest searchRequest = searchRequestParser.parse(input);
		final IndexRange indexRange = indexRangeParser.parse(input);
		final ResultList results = index.getDocuments().searchBy(searchRequest);
		final List<Result> displayedResults = results.subList(indexRange.fromIndex(), Math.min(indexRange.toIndex(), results.size()));
		
		Response openSearchResponse = new ResponseImpl();
		openSearchResponse.setTitle("Search results for \"" + searchRequest.getQuery() + "\"");
		
		net.lucenews3.opensearch.QueryList openSearchQueries = openSearchResponse.getQueries();
		net.lucenews3.opensearch.Query openSearchQuery = new net.lucenews3.opensearch.QueryImpl();
		openSearchQuery.setCount(10);
		openSearchQuery.setLanguage("en");
		openSearchQuery.setSearchTerms(searchRequest.getQuery().toString());
		openSearchQuery.setTotalResults(results.size());
		openSearchQueries.add(openSearchQuery);
		
		net.lucenews3.opensearch.ResultList openSearchResults = openSearchResponse.getResults();
		for (Result result : displayedResults) {
			final Document document = result.getDocument();
			final FieldList fields = document.getFields();
			net.lucenews3.opensearch.Result openSearchResult = new net.lucenews3.opensearch.ResultImpl();
			openSearchResult.setId("http://localhost:8080/lucene/" + indexIdentity + "/" + fields.byName("text").first().stringValue());
			openSearchResult.setRelevance(new Double(result.getScore()));
			openSearchResult.setTitle(fields.first().stringValue());
			openSearchResults.add(openSearchResult);
		}
		
		final ModelAndView result = new ModelAndView();
		result.addObject("indexIdentity", indexIdentity);
		result.addObject("index", index);
		result.addObject("searchRequest", searchRequest);
		result.addObject("indexRange", indexRange);
		result.addObject("results", results);
		result.addObject("displayedResults", displayedResults);
		result.addObject("response", openSearchResponse);
		result.addObject("bean", openSearchResponse);
		return result;
	}
	
	public IndexIdentityParser<I> getIndexIdentityParser() {
		return indexIdentityParser;
	}

	public void setIndexIdentityParser(IndexIdentityParser<I> indexIdentityParser) {
		this.indexIdentityParser = indexIdentityParser;
	}

	public Map<IndexIdentity, Index> getIndexesByIdentity() {
		return indexesByIdentity;
	}

	public void setIndexesByIdentity(Map<IndexIdentity, Index> indexesByIdentity) {
		this.indexesByIdentity = indexesByIdentity;
	}

	public SearchRequestParser<I> getSearchRequestParser() {
		return searchRequestParser;
	}

	public void setSearchRequestParser(SearchRequestParser<I> searchRequestParser) {
		this.searchRequestParser = searchRequestParser;
	}

	public IndexRangeParser<I> getIndexRangeParser() {
		return indexRangeParser;
	}

	public void setIndexRangeParser(IndexRangeParser<I> indexRangeParser) {
		this.indexRangeParser = indexRangeParser;
	}

}
