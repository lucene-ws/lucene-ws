package net.lucenews3.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.lucenews3.lucene.support.Index;
import net.lucenews3.lucene.support.IndexIdentity;
import net.lucenews3.lucene.support.IndexIdentityParser;
import net.lucenews3.lucene.support.ResultList;
import net.lucenews3.lucene.support.SearchRequest;
import net.lucenews3.lucene.support.SearchRequestParser;

import org.springframework.web.servlet.ModelAndView;

public class SearchIndexController extends AbstractController {
	
	private IndexIdentityParser<HttpServletRequest> indexIdentityParser;
	private SearchRequestParser<HttpServletRequest> searchRequestParser;
	private Map<IndexIdentity, Index> indexesByIdentity;
	
	public IndexIdentityParser<HttpServletRequest> getIndexIdentityParser() {
		return indexIdentityParser;
	}

	public void setIndexIdentityParser(IndexIdentityParser<HttpServletRequest> indexIdentityParser) {
		this.indexIdentityParser = indexIdentityParser;
	}

	public SearchRequestParser<HttpServletRequest> getSearchRequestParser() {
		return searchRequestParser;
	}

	public void setSearchRequestParser(SearchRequestParser<HttpServletRequest> searchRequestParser) {
		this.searchRequestParser = searchRequestParser;
	}

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		final IndexIdentity identity = indexIdentityParser.parse(request);
		final Index index = indexesByIdentity.get(identity);
		final SearchRequest searchRequest = searchRequestParser.parse(request);
		final ResultList results = index.getDocuments().searchBy(searchRequest);
		
		return new ModelAndView("search/results", "results", results);
	}

}
