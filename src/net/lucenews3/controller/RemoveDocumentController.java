package net.lucenews3.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.lucenews3.lucene.support.Index;

import org.springframework.web.servlet.ModelAndView;

public class RemoveDocumentController extends AbstractController {

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Index index = service.getIndex(request.getAttribute("index").toString());
		// TODO Auto-generated method stub
		return null;
	}

}
