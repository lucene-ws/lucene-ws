package net.lucenews3.controller;

import java.util.Map;

import net.lucenews3.lucene.support.Index;
import net.lucenews3.lucene.support.IndexIdentity;
import net.lucenews3.lucene.support.IndexIdentityParser;

import org.springframework.web.servlet.ModelAndView;

public class RemoveDocumentController<I, O> implements Controller<I, O> {

	private IndexIdentityParser<I> indexIdentityParser;
	private Map<IndexIdentity, Index> indexesByIdentity;
	
	@Override
	public ModelAndView handleRequest(I input, O output) throws Exception {
		// Resolve index
		final IndexIdentity indexIdentity = indexIdentityParser.parse(input);
		final Index index = indexesByIdentity.get(indexIdentity);
		
		
		final ModelAndView result = new ModelAndView("document/remove");
		result.addObject("indexIdentity", indexIdentity);
		result.addObject("index", index);
		return result;
	}

}
