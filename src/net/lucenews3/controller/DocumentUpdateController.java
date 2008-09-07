package net.lucenews3.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.lucenews3.ControllerSupport;
import net.lucenews3.DocumentIterator;
import net.lucenews3.DocumentMetaData;
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
			List<DocumentMetaData> documents = new ArrayList<DocumentMetaData>();
			while (iterator.hasNext()) {
				Document document = iterator.next();
				documents.add(index.updateDocument(document));
			}
			model.addObject("documents", documents);
		} else {
			Document document = iterator.next();
			model.addObject("document", index.updateDocument(document));
		}
		
		return model;
	}

}
