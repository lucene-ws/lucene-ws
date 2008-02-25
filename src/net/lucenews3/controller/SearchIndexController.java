package net.lucenews3.controller;

import java.util.List;
import java.util.Map;

import net.lucenews3.atom.LinkList;
import net.lucenews3.exception.NoSuchIndexException;
import net.lucenews3.http.Url;
import net.lucenews3.http.UrlParser;
import net.lucenews3.model.Document;
import net.lucenews3.model.FieldList;
import net.lucenews3.model.Index;
import net.lucenews3.model.IndexIdentity;
import net.lucenews3.model.IndexIdentityParser;
import net.lucenews3.model.IndexRange;
import net.lucenews3.model.IndexRangeParser;
import net.lucenews3.model.Notes;
import net.lucenews3.model.Page;
import net.lucenews3.model.QuerySpellChecker;
import net.lucenews3.model.Result;
import net.lucenews3.model.ResultList;
import net.lucenews3.model.ResultToOpenSearchResultTransformer;
import net.lucenews3.model.SearchRequest;
import net.lucenews3.model.SearchRequestParser;
import net.lucenews3.opensearch.Response;
import net.lucenews3.opensearch.ResponseImpl;
import net.lucenews3.opensearch.dom4j.ResponseBuilder;
import net.lucenews3.test.support.MapUtility;

import org.apache.lucene.queryParser.Token;
import org.apache.lucene.search.Query;
import org.dom4j.DocumentHelper;
import org.springframework.web.servlet.ModelAndView;

public class SearchIndexController<I, O> implements Controller<I, O> {
	
	private IndexIdentityParser<I> indexIdentityParser;
	private SearchRequestParser<I> searchRequestParser;
	private IndexRangeParser<I> indexRangeParser;
	private UrlParser<I> urlParser;
	private UrlParser<I> baseUrlParser;
	private Map<IndexIdentity, Index> indexesByIdentity;
	private QuerySpellChecker spellChecker;
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
		
		Url baseUrl = baseUrlParser.parse(input);
		
		Url url = urlParser.parse(input);
		
		net.lucenews3.atom.Link link = new net.lucenews3.atom.LinkImpl();
		link.setTitle("whoo!");
		link.setHref(url.toString());
		openSearchResponse.getLinks().add(link);
		
		Url previousUrl = url.clone();
		if (indexRange instanceof Page) {
			Page page = (Page) indexRange;
			previousUrl.getParameters().byKey().put("page", String.valueOf(page.getOrdinal() - 1));
			net.lucenews3.atom.Link previous = new net.lucenews3.atom.LinkImpl();
			previous.setRel("prev");
			previous.setTitle("Previous");
			previous.setHref(previousUrl.toString());
			openSearchResponse.getLinks().add(previous);
		} else {
			previousUrl.getParameters().byKey().put("startIndex", String.valueOf(indexRange.fromIndex() - (indexRange.toIndex() - indexRange.fromIndex())));
			net.lucenews3.atom.Link previous = new net.lucenews3.atom.LinkImpl();
			previous.setRel("prev");
			previous.setTitle("Previous");
			previous.setHref(previousUrl.toString());
			openSearchResponse.getLinks().add(previous);
		}
		
		Url nextUrl = url.clone();
		if (indexRange instanceof Page) {
			Page page = (Page) indexRange;
			nextUrl.getParameters().byKey().put("page", String.valueOf(page.getOrdinal() + 1));
			net.lucenews3.atom.Link next = new net.lucenews3.atom.LinkImpl();
			next.setRel("next");
			next.setTitle("Next");
			next.setHref(nextUrl.toString());
			openSearchResponse.getLinks().add(next);
		} else {
			nextUrl.getParameters().byKey().put("startIndex", String.valueOf(indexRange.toIndex()));
			net.lucenews3.atom.Link next = new net.lucenews3.atom.LinkImpl();
			next.setRel("next");
			next.setTitle("Next");
			next.setHref(nextUrl.toString());
			openSearchResponse.getLinks().add(next);
		}
		
		net.lucenews3.opensearch.QueryList openSearchQueries = openSearchResponse.getQueries();
		net.lucenews3.opensearch.Query openSearchQuery = new net.lucenews3.opensearch.QueryImpl();
		openSearchQuery.setRole("request");
		openSearchQuery.setCount(10);
		openSearchQuery.setLanguage("en");
		org.apache.lucene.search.Query query = searchRequest.getQuery();
		Object note = Notes.get(query, "token");
		if (note != null && note instanceof Token) {
			Token token = (Token) note;
			openSearchQuery.setSearchTerms(token.image);
			openSearchResponse.setTitle("Search results for \"" + token.image + "\"");
		} else {
			if (searchRequest.getQuery() == null) {
				openSearchQuery.setSearchTerms(null);
				openSearchResponse.setTitle("Documents");
			} else {
				openSearchQuery.setSearchTerms(searchRequest.getQuery().toString());
				openSearchResponse.setTitle("Search results for \"" + searchRequest.getQuery() + "\"");
			}
		}
		openSearchQuery.setTotalResults(results.size());
		openSearchQueries.add(openSearchQuery);
		
		if (spellChecker != null) {
			List<Query> suggestions = spellChecker.suggestSimilar(query);
			for (Query suggestion : suggestions) {
				net.lucenews3.opensearch.Query correction = new net.lucenews3.opensearch.QueryImpl();
				correction.setRole("correction");
				correction.setSearchTerms(suggestion.toString());
				openSearchQueries.add(correction);
			}
		}
		
		String primaryField = index.getMetaData().getPrimaryField();
		
		net.lucenews3.opensearch.ResultList openSearchResults = openSearchResponse.getResults();
		for (Result result : displayedResults) {
			final Document document = result.getDocument();
			final FieldList fields = document.getFields();
			
			final net.lucenews3.opensearch.Result openSearchResult = new ResultToOpenSearchResultTransformer().transform(result);
			//openSearchResult.setId("http://localhost:8080/lucene/" + indexIdentity + "/" + fields.byName("text").first().stringValue());
			final LinkList resultLinks = openSearchResult.getLinks();
			final net.lucenews3.atom.Link resultLink = new net.lucenews3.atom.LinkImpl();
			
			final String documentIdentity = fields.byName(primaryField).first().stringValue();
			
			Url resultUrl = baseUrl.clone();
			resultUrl.getPath().add(indexIdentity.toString());
			resultUrl.getPath().add(documentIdentity);
			resultLink.setHref(resultUrl.toString());
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

	public UrlParser<I> getUrlParser() {
		return urlParser;
	}

	public void setUrlParser(UrlParser<I> urlParser) {
		this.urlParser = urlParser;
	}

	public UrlParser<I> getBaseUrlParser() {
		return baseUrlParser;
	}

	public void setBaseUrlParser(UrlParser<I> baseUrlParser) {
		this.baseUrlParser = baseUrlParser;
	}

	public QuerySpellChecker getSpellChecker() {
		return spellChecker;
	}

	public void setSpellChecker(QuerySpellChecker spellChecker) {
		this.spellChecker = spellChecker;
	}

}
