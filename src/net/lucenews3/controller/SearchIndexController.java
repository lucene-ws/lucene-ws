package net.lucenews3.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.lucenews3.lucene.support.Index;
import net.lucenews3.lucene.support.ResultList;
import net.lucenews3.lucene.support.SearchRequest;
import net.lucenews3.lucene.support.SearchRequestParser;

import org.springframework.web.servlet.ModelAndView;

public class SearchIndexController extends AbstractController {
	
	private SearchRequestParser<HttpServletRequest> searchRequestParser;
	
	public SearchRequestParser<HttpServletRequest> getSearchRequestParser() {
		return searchRequestParser;
	}

	public void setSearchRequestParser(
			SearchRequestParser<HttpServletRequest> searchRequestParser) {
		this.searchRequestParser = searchRequestParser;
	}

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		final Index index = service.getIndex(request.getAttribute("indexName").toString());
		final SearchRequest searchRequest = searchRequestParser.parseSearchRequest(request);
		final ResultList results = index.getDocuments().searchBy(searchRequest);
		
		return new ModelAndView("search/results", "results", results);
	}

}
