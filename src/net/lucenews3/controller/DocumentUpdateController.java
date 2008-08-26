package net.lucenews3.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.lucenews3.ControllerSupport;
import net.lucenews3.DocumentIterator;
import net.lucenews3.Index;

import org.apache.lucene.document.Document;
import org.springframework.web.servlet.ModelAndView;

public class DocumentUpdateController extends ControllerSupport {

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView model = new ModelAndView("document/update");
		Index index = service.getIndex(request);
		
		DocumentIterator iterator = service.getDocumentIterator(request);
		
		if (iterator.isCollection()) {
			List<Document> documents = new ArrayList<Document>();
			while (iterator.hasNext()) {
				Document document = iterator.next();
				index.updateDocument(document);
				documents.add(document);
			}
			model.addObject("documents", documents);
		} else {
			Document document = iterator.next();
			index.updateDocument(document);
			model.addObject("document", document);
		}
		
		return model;
	}

}
