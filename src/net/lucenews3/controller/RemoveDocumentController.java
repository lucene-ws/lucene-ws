package net.lucenews3.controller;

import java.util.Map;

import net.lucenews3.model.DocumentIdentity;
import net.lucenews3.model.DocumentIdentityParser;
import net.lucenews3.model.Index;
import net.lucenews3.model.IndexIdentity;
import net.lucenews3.model.IndexIdentityParser;

import org.springframework.web.servlet.ModelAndView;

public class RemoveDocumentController<I, O> implements Controller<I, O> {

	private IndexIdentityParser<I> indexIdentityParser;
	private Map<IndexIdentity, Index> indexesByIdentity;
	private DocumentIdentityParser<I> documentIdentityParser;
	
	@Override
	public ModelAndView handleRequest(I input, O output) throws Exception {
		// Resolve index
		final IndexIdentity indexIdentity = indexIdentityParser.parse(input);
		final Index index = indexesByIdentity.get(indexIdentity);
		final DocumentIdentity documentIdentity = documentIdentityParser.parse(input);
		
		
		final ModelAndView result = new ModelAndView("document/remove");
		result.addObject("indexIdentity", indexIdentity);
		result.addObject("index", index);
		result.addObject("documentIdentity", documentIdentity);
		return result;
	}

	public IndexIdentityParser<I> getIndexIdentityParser() {
		return indexIdentityParser;
	}

	public void setIndexIdentityParser(IndexIdentityParser<I> indexIdentityParser) {
		this.indexIdentityParser = indexIdentityParser;
	}

	public Map<IndexIdentity, Index> getIndexesByIdentity() {
		return indexesByIdentity;
	}

	public void setIndexesByIdentity(Map<IndexIdentity, Index> indexesByIdentity) {
		this.indexesByIdentity = indexesByIdentity;
	}

	public DocumentIdentityParser<I> getDocumentIdentityParser() {
		return documentIdentityParser;
	}

	public void setDocumentIdentityParser(
			DocumentIdentityParser<I> documentIdentityParser) {
		this.documentIdentityParser = documentIdentityParser;
	}

}
