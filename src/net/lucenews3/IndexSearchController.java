package net.lucenews3;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.springframework.web.servlet.ModelAndView;

public class IndexSearchController extends ControllerSupport {

	private String queryParameterName;

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest req, HttpServletResponse res) throws Exception {
		ModelAndView model = new ModelAndView("search/results");
		
		String queryString = req.getParameter(queryParameterName);
		
		Query query;
		if (queryString == null) {
			query = null;
		} else {
			query = null;
		}
		
		Index index = service.getIndex(req);
		Results results = index.search(req);
		model.addObject("results", results);
		
		return model;
	}

}
