package net.lucenews3.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.lucenews3.ControllerSupport;
import net.lucenews3.DocumentIterator;
import net.lucenews3.Index;

import org.apache.lucene.document.Document;
import org.springframework.web.servlet.ModelAndView;

public class DocumentCreateController extends ControllerSupport {

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView model = new ModelAndView("index/post");
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

	protected void addDocument(Index index, Document document, HttpServletRequest request, HttpServletResponse response) throws IOException {
		System.out.println("Inserting document " + document);
		index.insertDocument(document);
		response.addHeader("Location", service.getDocumentURI(request, index, document));
	}
}
