package net.lucenews3.controller;

import java.util.List;
import java.util.Map;

import net.lucenews3.exception.NoSuchIndexException;
import net.lucenews3.lucene.support.Index;
import net.lucenews3.lucene.support.IndexIdentity;
import net.lucenews3.lucene.support.IndexIdentityParser;
import net.lucenews3.lucene.support.IndexRange;
import net.lucenews3.lucene.support.IndexRangeParser;
import net.lucenews3.lucene.support.Result;
import net.lucenews3.lucene.support.ResultList;
import net.lucenews3.lucene.support.SearchRequest;
import net.lucenews3.lucene.support.SearchRequestParser;

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
		
		final ModelAndView result = new ModelAndView();
		result.addObject("indexIdentity", indexIdentity);
		result.addObject("index", index);
		result.addObject("searchRequest", searchRequest);
		result.addObject("indexRange", indexRange);
		result.addObject("results", results);
		result.addObject("displayedResults", displayedResults);
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
