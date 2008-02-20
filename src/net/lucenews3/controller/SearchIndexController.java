package net.lucenews3.controller;

import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.lucenews3.exception.NoSuchIndexException;
import net.lucenews3.lucene.support.Document;
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
import org.springframework.web.servlet.View;

public class SearchIndexController extends AbstractController {
	
	private IndexIdentityParser<HttpServletRequest> indexIdentityParser;
	private SearchRequestParser<HttpServletRequest> searchRequestParser;
	private Map<IndexIdentity, Index> indexesByIdentity;
	private IndexRangeParser<HttpServletRequest> indexRangeParser;

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		final IndexIdentity indexIdentity = indexIdentityParser.parse(request);
		final Index index = indexesByIdentity.get(indexIdentity);
		
		if (index == null) {
			throw new NoSuchIndexException(indexIdentity);
		}
		
		final SearchRequest searchRequest = searchRequestParser.parse(request);
		final IndexRange indexRange = indexRangeParser.parse(request);
		final ResultList results = index.getDocuments().searchBy(searchRequest);
		final ResultList displayedResults = results.subList(indexRange.fromIndex(), indexRange.toIndex());
		
		//return new ModelAndView("search/results", "results", results);
		return new ModelAndView(new View() {

			@Override
			public String getContentType() {
				return "text/plain";
			}

			@SuppressWarnings("unchecked")
			@Override
			public void render(Map arg0, HttpServletRequest arg1,
					HttpServletResponse response) throws Exception {
				PrintWriter out = response.getWriter();
				out.println("Search for \"" + searchRequest.getQuery() + "\" returned " + results.size() + " results!");
				out.println(index.getDocuments().size() + " documents");
				for (Result result : results) {
					Document document = result.getDocument();
					out.println(result.getNumber() + " " + document.getFields().byName("text").only().stringValue());
				}
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
