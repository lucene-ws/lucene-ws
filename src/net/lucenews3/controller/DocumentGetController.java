package net.lucenews3.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.lucenews3.ControllerSupport;
import net.lucenews3.Index;

import org.springframework.web.servlet.ModelAndView;

public class DocumentGetController extends ControllerSupport {

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest req, HttpServletResponse res) throws Exception {
		ModelAndView model = new ModelAndView("document/view");
		
		String document = (String) req.getAttribute("document");
		Index index = service.getIndex(req);
		model.addObject("document", index.getDocument(document));
		
		return model;
	}

}
