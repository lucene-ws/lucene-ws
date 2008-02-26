package net.lucenews3.controller;

import java.util.Map;

import net.lucenews3.Transformer;
import net.lucenews3.exception.NoSuchIndexException;
import net.lucenews3.http.Url;
import net.lucenews3.http.UrlParser;
import net.lucenews3.model.Index;
import net.lucenews3.model.IndexIdentity;
import net.lucenews3.model.IndexIdentityParser;
import net.lucenews3.model.IndexRange;
import net.lucenews3.model.IndexRangeParser;
import net.lucenews3.model.QuerySpellChecker;
import net.lucenews3.model.ResultList;
import net.lucenews3.model.SearchContext;
import net.lucenews3.model.SearchContextImpl;
import net.lucenews3.model.SearchRequest;
import net.lucenews3.model.SearchRequestParser;

import org.dom4j.Document;
import org.springframework.web.servlet.ModelAndView;

public class SearchIndexController<I, O> implements Controller<I, O> {
	
	private Transformer<SearchContext, Document> searchContextTransformer;
	private IndexIdentityParser<I> indexIdentityParser;
	private SearchRequestParser<I> searchRequestParser;
	private IndexRangeParser<I> indexRangeParser;
	private UrlParser<I> urlParser;
	private UrlParser<I> baseUrlParser;
	private Map<IndexIdentity, Index> indexesByIdentity;
	private QuerySpellChecker spellChecker;
	
	/**
	 * Performs a search on the specified index. Transforms the given search results
	 * into an OpenSearch response.
	 */
	@Override
	public ModelAndView handleRequest(I input, O output) throws Exception {
		final SearchContext searchContext = new SearchContextImpl();
		
		final IndexIdentity indexIdentity = indexIdentityParser.parse(input);
		searchContext.setIndexIdentity(indexIdentity);
		
		final Index index = indexesByIdentity.get(indexIdentity);
		searchContext.setIndex(index);
		
		if (index == null) {
			throw new NoSuchIndexException(indexIdentity);
		}
		
		final SearchRequest searchRequest = searchRequestParser.parse(input);
		searchContext.setSearchRequest(searchRequest);
		
		final IndexRange indexRange = indexRangeParser.parse(input);
		searchContext.setIndexRange(indexRange);
		
		final ResultList results = index.getDocuments().searchBy(searchRequest);
		searchContext.setResults(results);
		
		final Url url = urlParser.parse(input);
		searchContext.setUrl(url);
		
		final Url baseUrl = baseUrlParser.parse(input);
		searchContext.setBaseUrl(baseUrl);
		
		final Document document = searchContextTransformer.transform(searchContext);
		
		final ModelAndView result = new ModelAndView();
		result.addObject("indexIdentity", indexIdentity);
		result.addObject("index", index);
		result.addObject("searchRequest", searchRequest);
		result.addObject("indexRange", indexRange);
		result.addObject("results", results);
		result.addObject("document", document);
		return result;
	}
	
	public Transformer<SearchContext, Document> getSearchContextTransformer() {
		return searchContextTransformer;
	}

	public void setSearchContextTransformer(
			Transformer<SearchContext, Document> searchContextTransformer) {
		this.searchContextTransformer = searchContextTransformer;
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
