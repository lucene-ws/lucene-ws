package net.lucenews3.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class RemoveIndexController implements Controller {

	private IndexAccess indexAccess;
	
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String indexName = request.getAttribute("index").toString();
		indexAccess.remove(indexName);
		
		return null;
	}

}
