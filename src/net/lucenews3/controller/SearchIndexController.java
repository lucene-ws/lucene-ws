package net.lucenews3.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.lucenews3.exception.NoSuchIndexException;
import net.lucenews3.lucene.support.Index;
import net.lucenews3.lucene.support.IndexIdentity;
import net.lucenews3.lucene.support.IndexIdentityParser;
import net.lucenews3.lucene.support.ResultList;
import net.lucenews3.lucene.support.SearchRequest;
import net.lucenews3.lucene.support.SearchRequestParser;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

public class SearchIndexController extends AbstractController {
	
	private IndexIdentityParser<HttpServletRequest> indexIdentityParser;
	private SearchRequestParser<HttpServletRequest> searchRequestParser;
	private Map<IndexIdentity, Index> indexesByIdentity;

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		final IndexIdentity indexIdentity = indexIdentityParser.parse(request);
		final Index index = indexesByIdentity.get(indexIdentity);
		
		if (index == null) {
			throw new NoSuchIndexException(indexIdentity);
		}
		
		final SearchRequest searchRequest = searchRequestParser.parse(request);
		final ResultList results = index.getDocuments().searchBy(searchRequest);
		
		//return new ModelAndView("search/results", "results", results);
		return new ModelAndView(new View() {

			@Override
			public String getContentType() {
				return "text/plain";
			}

			@Override
			public void render(Map arg0, HttpServletRequest arg1,
					HttpServletResponse response) throws Exception {
				response.getWriter().println("Search for \"" + searchRequest.getQuery() + "\" returned " + results.size() + " results!");
			}
		});
	}
	
	public IndexIdentityParser<HttpServletRequest> getIndexIdentityParser() {
		return indexIdentityParser;
	}

	public void setIndexIdentityParser(IndexIdentityParser<HttpServletRequest> indexIdentityParser) {
		this.indexIdentityParser = indexIdentityParser;
	}

	public Map<IndexIdentity, Index> getIndexesByIdentity() {
		return indexesByIdentity;
	}

	public void setIndexesByIdentity(Map<IndexIdentity, Index> indexesByIdentity) {
		this.indexesByIdentity = indexesByIdentity;
	}

	public SearchRequestParser<HttpServletRequest> getSearchRequestParser() {
		return searchRequestParser;
	}

	public void setSearchRequestParser(SearchRequestParser<HttpServletRequest> searchRequestParser) {
		this.searchRequestParser = searchRequestParser;
	}

}
