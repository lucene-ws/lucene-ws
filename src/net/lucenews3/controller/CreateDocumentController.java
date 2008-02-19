package net.lucenews3.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.lucenews3.lucene.support.DocumentList;
import net.lucenews3.lucene.support.Index;
import net.lucenews3.lucene.support.IndexIdentity;
import net.lucenews3.lucene.support.IndexIdentityParser;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class CreateDocumentController implements Controller {

	private IndexIdentityParser<HttpServletRequest> indexIdentityParser;
	private Map<IndexIdentity, Index> indexesByIdentity;
	
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// Resolve index
		final IndexIdentity indexIdentity = indexIdentityParser.parse(request);
		final Index index = indexesByIdentity.get(indexIdentity);
		
		// Resolve documents
		final DocumentList documents = null;
		index.getDocuments().addAll(documents);
		
		return new ModelAndView("document/create", "documents", documents);
	}

}
