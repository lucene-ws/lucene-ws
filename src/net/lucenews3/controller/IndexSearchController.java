package net.lucenews3.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.lucenews3.ControllerSupport;
import net.lucenews3.Index;

import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;
import org.springframework.web.servlet.ModelAndView;

public class IndexSearchController extends ControllerSupport {

	private String queryParameterName;

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView model = new ModelAndView("search/results");
		Index index = service.getIndex(request);
		
		String queryString = request.getParameter(queryParameterName);
		
		Query query;
		if (queryString == null) {
			query = null;
		} else {
			QueryParser parser = index.getQueryParser();
			query = parser.parse(queryString);
		}
		
		Searcher searcher = index.getSearcher();
		Hits hits = searcher.search(query == null ? new MatchAllDocsQuery() : query);
		model.addObject("hits", hits);
		
		return model;
	}

}
