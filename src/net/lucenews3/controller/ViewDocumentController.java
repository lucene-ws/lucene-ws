package net.lucenews3.controller;

import java.util.Map;

import net.lucenews3.model.Document;
import net.lucenews3.model.DocumentIdentity;
import net.lucenews3.model.DocumentIdentityParser;
import net.lucenews3.model.DocumentList;
import net.lucenews3.model.FieldListToXoxoTransformer;
import net.lucenews3.model.Index;
import net.lucenews3.model.IndexIdentity;
import net.lucenews3.model.IndexIdentityParser;
import net.lucenews3.model.IndexMetaData;

import org.apache.lucene.index.Term;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;
import org.springframework.web.servlet.ModelAndView;

public class ViewDocumentController<I, O> implements Controller<I, O> {

	private IndexIdentityParser<I> indexIdentityParser;
	private Map<IndexIdentity, Index> indexesByIdentity;
	private DocumentIdentityParser<I> documentIdentityParser;
	private Namespace atomNamespace;
	private Namespace xhtmlNamespace;
	private FieldListToXoxoTransformer xoxoTransformer;
	
	public ViewDocumentController() {
		this.xoxoTransformer = new FieldListToXoxoTransformer();
	}
	
	public ModelAndView handleRequest(I input, O output) throws Exception {
		final IndexIdentity indexIdentity = indexIdentityParser.parse(input);
		final Index index = indexesByIdentity.get(indexIdentity);
		final DocumentIdentity documentIdentity = documentIdentityParser.parse(input);
		
		final IndexMetaData indexMetaData = index.getMetaData();
		final String primaryFieldName = indexMetaData.getPrimaryField();
		final Term primaryTerm = new Term(primaryFieldName, documentIdentity.toString());
		final DocumentList documents = index.getDocuments().byTerm(primaryTerm);
		
		if (documents.isEmpty()) {
			throw new RuntimeException("Document \"" + documentIdentity + "\" not found");
		} else if (documents.size() > 1) {
			throw new RuntimeException("Multiple documents for \"" + documentIdentity + "\"");
		}
		final Document document = documents.get(0);
		
		final org.dom4j.Document xmlDocument = DocumentHelper.createDocument();
		
		final Element entry = DocumentHelper.createElement(QName.get("entry", atomNamespace));
		xmlDocument.add(entry);
		
		final Element content = DocumentHelper.createElement(QName.get("content", xhtmlNamespace));
		entry.add(content);
		
		final Element div = DocumentHelper.createElement(QName.get("div", xhtmlNamespace));
		content.add(div);
		
		final Element dl = xoxoTransformer.transform(document.getFields());
		div.add(dl);
		
		final ModelAndView result = new ModelAndView("document/view");
		result.addObject("indexIdentity", indexIdentity);
		result.addObject("index", index);
		result.addObject("entry", xmlDocument);
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

	public Namespace getAtomNamespace() {
		return atomNamespace;
	}

	public void setAtomNamespace(Namespace atomNamespace) {
		this.atomNamespace = atomNamespace;
	}

	public Namespace getXhtmlNamespace() {
		return xhtmlNamespace;
	}

	public void setXhtmlNamespace(Namespace xhtmlNamespace) {
		this.xhtmlNamespace = xhtmlNamespace;
	}

}
