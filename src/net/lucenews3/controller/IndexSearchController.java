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

	public static final String DEFAULT_SEARCH_TERMS_PARAMETER_NAME = "searchTerms";
	public static final String DEFAULT_COUNT_PARAMETER_NAME        = "count";
	public static final String DEFAULT_START_INDEX_PARAMETER_NAME  = "startIndex";
	public static final String DEFAULT_START_PAGE_PARAMETER_NAME   = "startPage";

	private String searchTermsParameterName;
	private String countParameterName;
	private String startIndexParameterName;
	private String startPageParameterName;

	public IndexSearchController() {
		this.searchTermsParameterName = DEFAULT_SEARCH_TERMS_PARAMETER_NAME;
		this.countParameterName       = DEFAULT_COUNT_PARAMETER_NAME;
		this.startIndexParameterName  = DEFAULT_START_INDEX_PARAMETER_NAME;
		this.startPageParameterName   = DEFAULT_START_PAGE_PARAMETER_NAME;
	}

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView model = new ModelAndView("search/results");
		Index index = service.getIndex(request);
		
		String searchTerms = request.getParameter(searchTermsParameterName);
		
		Query query;
		if (searchTerms == null) {
			query = null;
		} else {
			QueryParser parser = index.getQueryParser();
			query = parser.parse(searchTerms);
		}
		
		Searcher searcher = index.getSearcher();
		Hits hits = searcher.search(query == null ? new MatchAllDocsQuery() : query);
		model.addObject("hits", hits);
		
		return model;
	}

	public String getSearchTermsParameterName() {
		return searchTermsParameterName;
	}

	public void setSearchTermsParameterName(String searchTermsParameterName) {
		this.searchTermsParameterName = searchTermsParameterName;
	}

	public String getCountParameterName() {
		return countParameterName;
	}

	public void setCountParameterName(String countParameterName) {
		this.countParameterName = countParameterName;
	}

	public String getStartIndexParameterName() {
		return startIndexParameterName;
	}

	public void setStartIndexParameterName(String startIndexParameterName) {
		this.startIndexParameterName = startIndexParameterName;
	}

	public String getStartPageParameterName() {
		return startPageParameterName;
	}

	public void setStartPageParameterName(String startPageParameterName) {
		this.startPageParameterName = startPageParameterName;
	}

}
