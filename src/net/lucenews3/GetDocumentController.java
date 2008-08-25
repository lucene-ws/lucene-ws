package net.lucenews3;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.lucene.document.Document;
import org.springframework.web.servlet.ModelAndView;

public class GetDocumentController extends ControllerSupport {

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView model = new ModelAndView("document/get");
		
		Index index = service.getIndex(request);
		Document document = index.getDocument((String) request.getAttribute("document"));
		model.addObject("document", document);
		
		return model;
	}

}
