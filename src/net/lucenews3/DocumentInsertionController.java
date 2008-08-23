package net.lucenews3;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.lucene.document.Document;
import org.springframework.web.servlet.ModelAndView;

public class DocumentInsertionController extends ControllerSupport {

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView model = new ModelAndView("document/add");
		Index index = service.getIndex(request);
		
		DocumentIterator iterator = service.getDocumentIterator(request);
		if (iterator.isCollection()) {
			List<Document> documents = new ArrayList<Document>();
			while (iterator.hasNext()) {
				Document document = iterator.next();
				addDocument(index, document, request, response);
				documents.add(document);
			}
			model.addObject("documents", documents);
		} else {
			Document document = iterator.next();
			addDocument(index, document, request, response);
			model.addObject("document", document);
		}
		
		return model;
	}

	protected void addDocument(Index index, Document document, HttpServletRequest request, HttpServletResponse response) {
		index.addDocument(document);
		response.addHeader("Location", service.getDocumentURI(request, index, document));
	}
}