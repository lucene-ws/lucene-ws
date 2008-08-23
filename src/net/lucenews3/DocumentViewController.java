package net.lucenews3;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

public class DocumentViewController extends ControllerSupport {

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest req, HttpServletResponse res) throws Exception {
		ModelAndView model = new ModelAndView("document/view");
		
		String document = (String) req.getAttribute("document");
		Index index = service.getIndex(req);
		model.addObject("document", index.getDocument(document));
		
		return model;
	}

}