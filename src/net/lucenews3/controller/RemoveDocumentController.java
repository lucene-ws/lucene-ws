package net.lucenews3.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.lucenews3.lucene.support.Index;
import net.lucenews3.lucene.support.IndexIdentity;
import net.lucenews3.lucene.support.IndexIdentityParser;

import org.springframework.web.servlet.ModelAndView;

public class RemoveDocumentController extends AbstractController {

	private IndexIdentityParser<HttpServletRequest> indexIdentityParser;
	private Map<IndexIdentity, Index> indexesByIdentity;
	
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// Resolve index
		final IndexIdentity indexIdentity = indexIdentityParser.parse(request);
		final Index index = indexesByIdentity.get(indexIdentity);
		
		
		
		return new ModelAndView("document/remove", "index", index);
	}

}
