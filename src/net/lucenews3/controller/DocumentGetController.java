package net.lucenews3.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.lucenews3.ControllerSupport;
import net.lucenews3.Index;

import org.springframework.web.servlet.ModelAndView;

public class DocumentGetController extends ControllerSupport {

	public static final String DEFAULT_DOCUMENT_ATTRIBUTE_NAME = "document";

	private String documentAttributeName;

	public DocumentGetController() {
		this.documentAttributeName = DEFAULT_DOCUMENT_ATTRIBUTE_NAME;
	}

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView model = new ModelAndView("document/view");
		
		String document = (String) request.getAttribute(documentAttributeName);
		Index index = service.getIndex(request);
		model.addObject("document", index.getDocument(document));
		
		return model;
	}

	public String getDocumentAttributeName() {
		return documentAttributeName;
	}

	public void setDocumentAttributeName(String documentAttributeName) {
		this.documentAttributeName = documentAttributeName;
	}

}
