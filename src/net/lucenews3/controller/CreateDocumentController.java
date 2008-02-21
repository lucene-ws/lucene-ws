package net.lucenews3.controller;

import java.util.List;
import java.util.Map;

import net.lucenews3.model.Document;
import net.lucenews3.model.DocumentListParser;
import net.lucenews3.model.Index;
import net.lucenews3.model.IndexIdentity;
import net.lucenews3.model.IndexIdentityParser;

import org.springframework.web.servlet.ModelAndView;

public class CreateDocumentController<I, O> implements Controller<I, O> {

	private IndexIdentityParser<I> indexIdentityParser;
	private DocumentListParser<I> documentListParser;
	private Map<IndexIdentity, Index> indexesByIdentity;
	
	public ModelAndView handleRequest(I input, O output) throws Exception {
		final IndexIdentity indexIdentity = indexIdentityParser.parse(input);
		final Index index = indexesByIdentity.get(indexIdentity);
		final List<Document> documents = documentListParser.parse(input);
		
		index.getDocuments().addAll(documents);
		
		final ModelAndView result = new ModelAndView("document/create");
		result.addObject("indexIdentity", indexIdentity);
		result.addObject("index", index);
		result.addObject("documents", documents);
		return result;
	}

	public IndexIdentityParser<I> getIndexIdentityParser() {
		return indexIdentityParser;
	}

	public void setIndexIdentityParser(IndexIdentityParser<I> indexIdentityParser) {
		this.indexIdentityParser = indexIdentityParser;
	}

	public DocumentListParser<I> getDocumentListParser() {
		return documentListParser;
	}

	public void setDocumentListParser(DocumentListParser<I> documentListParser) {
		this.documentListParser = documentListParser;
	}

	public Map<IndexIdentity, Index> getIndexesByIdentity() {
		return indexesByIdentity;
	}

	public void setIndexesByIdentity(Map<IndexIdentity, Index> indexesByIdentity) {
		this.indexesByIdentity = indexesByIdentity;
	}

}
