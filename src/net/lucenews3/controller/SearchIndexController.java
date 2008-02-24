package net.lucenews3.controller;

import java.util.List;
import java.util.Map;

import net.lucenews3.atom.LinkList;
import net.lucenews3.exception.NoSuchIndexException;
import net.lucenews3.model.Index;
import net.lucenews3.model.IndexIdentity;
import net.lucenews3.model.IndexIdentityParser;
import net.lucenews3.model.IndexRange;
import net.lucenews3.model.IndexRangeParser;
import net.lucenews3.model.Result;
import net.lucenews3.model.ResultList;
import net.lucenews3.model.ResultToOpenSearchResultTransformer;
import net.lucenews3.model.SearchRequest;
import net.lucenews3.model.SearchRequestParser;
import net.lucenews3.model.TextSource;
import net.lucenews3.opensearch.Response;
import net.lucenews3.opensearch.ResponseImpl;
import net.lucenews3.opensearch.dom4j.ResponseBuilder;
import net.lucenews3.test.support.MapUtility;

import org.dom4j.DocumentHelper;
import org.springframework.web.servlet.ModelAndView;

public class SearchIndexController<I, O> implements Controller<I, O> {
	
	private IndexIdentityParser<I> indexIdentityParser;
	private SearchRequestParser<I> searchRequestParser;
	private IndexRangeParser<I> indexRangeParser;
	private Map<IndexIdentity, Index> indexesByIdentity;
	private MapUtility maps;

	public SearchIndexController() {
		this.maps = new MapUtility();
	}
	
	/**
	 * Performs a search on the specified index. Transforms the given search results
	 * into an OpenSearch response.
	 */
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
		openSearchResponse.setTotalResults(results.size());
		openSearchResponse.setTitle("Search results for \"" + searchRequest.getQuery() + "\"");
		openSearchResponse.setStartIndex(indexRange.fromIndex() + 1);
		
		net.lucenews3.atom.Link link = new net.lucenews3.atom.LinkImpl();
		link.setTitle("whoo!");
		openSearchResponse.getLinks().add(link);
		
		net.lucenews3.atom.Link next = new net.lucenews3.atom.LinkImpl();
		next.setRel("next");
		next.setTitle("Next page");
		next.setHref("/lucene3/christmasCarol?page=2");
		openSearchResponse.getLinks().add(next);
		
		net.lucenews3.opensearch.QueryList openSearchQueries = openSearchResponse.getQueries();
		net.lucenews3.opensearch.Query openSearchQuery = new net.lucenews3.opensearch.QueryImpl();
		openSearchQuery.setRole("request");
		openSearchQuery.setCount(10);
		openSearchQuery.setLanguage("en");
		org.apache.lucene.search.Query query = searchRequest.getQuery();
		if (query instanceof TextSource) {
			openSearchQuery.setSearchTerms(((TextSource) query).getText());
		} else {
			openSearchQuery.setSearchTerms(searchRequest.getQuery().toString());
		}
		openSearchQuery.setTotalResults(results.size());
		openSearchQueries.add(openSearchQuery);
		
		net.lucenews3.opensearch.Query correction = new net.lucenews3.opensearch.QueryImpl();
		correction.setRole("correction");
		correction.setSearchTerms("puppy");
		openSearchQueries.add(correction);
		
		net.lucenews3.opensearch.ResultList openSearchResults = openSearchResponse.getResults();
		for (Result result : displayedResults) {
			final net.lucenews3.opensearch.Result openSearchResult = new ResultToOpenSearchResultTransformer().transform(result);
			//openSearchResult.setId("http://localhost:8080/lucene/" + indexIdentity + "/" + fields.byName("text").first().stringValue());
			final LinkList resultLinks = openSearchResult.getLinks();
			final net.lucenews3.atom.Link resultLink = new net.lucenews3.atom.LinkImpl();
			resultLink.setHref("/lucene3/something");
			resultLink.setTitle("Document!");
			resultLinks.add(resultLink);
			openSearchResults.add(openSearchResult);
		}
		
		org.dom4j.Document document = DocumentHelper.createDocument();
		ResponseBuilder builder = new ResponseBuilder();
		org.dom4j.Element element = builder.build(openSearchResponse);
		document.add(DocumentHelper.createProcessingInstruction("xml-stylesheet", maps.toMap("type", "text/xsl", "href", "/lucene3/static/searchResults.xslt")));
		document.add(element);
		
		final ModelAndView result = new ModelAndView();
		result.addObject("indexIdentity", indexIdentity);
		result.addObject("index", index);
		result.addObject("searchRequest", searchRequest);
		result.addObject("indexRange", indexRange);
		result.addObject("results", results);
		result.addObject("displayedResults", displayedResults);
		result.addObject("response", openSearchResponse);
		result.addObject("document", document);
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
