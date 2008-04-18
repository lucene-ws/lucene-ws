package net.lucenews3.opensearch.transform;

import net.lucenews3.atom.Link;
import net.lucenews3.atom.LinkImpl;
import net.lucenews3.atom.dom4j.AbstractBuilder;
import net.lucenews3.atom.dom4j.LinkBuilder;
import net.lucenews3.http.Url;
import net.lucenews3.model.Document;
import net.lucenews3.model.Field;
import net.lucenews3.model.FieldList;
import net.lucenews3.model.FieldListToXoxoTransformer;
import net.lucenews3.model.Index;
import net.lucenews3.model.IndexIdentity;
import net.lucenews3.model.Result;
import net.lucenews3.model.SearchContext;
import net.lucenews3.model.SearchRequest;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;

public class OpenSearchResultTransformer extends AbstractBuilder {

	private Namespace atomNamespace;
	private Namespace openSearchNamespace;
	private Namespace relevanceNamespace;
	private Namespace xhtmlNamespace;
	private LinkBuilder linkBuilder;
	private FieldListToXoxoTransformer fieldListTransformer;
	
	public OpenSearchResultTransformer() {
		this.linkBuilder = new LinkBuilder();
		this.atomNamespace = Namespace.get("atom", "http://www.w3.org/2005/Atom");
		this.openSearchNamespace = Namespace.get("opensearch", "http://a9.com/-/spec/opensearch/1.1/");
		this.relevanceNamespace = Namespace.get("relevance", "http://a9.com/-/opensearch/extensions/relevance/1.0/");
		this.xhtmlNamespace = Namespace.get("xhtml", "http://www.w3.org/1999/xhtml");
		this.fieldListTransformer = new FieldListToXoxoTransformer();
	}
	
	public Element transform(final SearchContext context, final Result result) {
		final Element element = DocumentHelper.createElement(QName.get("entry", atomNamespace));
		transform(context, result, element);
		return element;
	}

	public void transform(final SearchContext context, final Result result, final Element entry) {
		final IndexIdentity indexIdentity = context.getIndexIdentity();
		final Index index = context.getIndex();
		final Document document = result.getDocument();
		final FieldList fields = document.getFields();
		final SearchRequest searchRequest = context.getSearchRequest();
		final String primaryFieldName = index.getMetaData().getPrimaryField();
		
		// Title
		addProperty(entry, QName.get("title", atomNamespace), "title", false);
		
		// Link
		if (fields.byName(primaryFieldName).isEmpty()) {
			// This document is unidentified
		} else {
			final Field primaryField = fields.byName(primaryFieldName).first();
			final Url linkUrl = context.getBaseUrl().clone();
			linkUrl.getPath().add(indexIdentity.toString());
			linkUrl.getPath().add(primaryField.stringValue());
			final Link link = new LinkImpl();
			link.setHref(linkUrl.toString());
			entry.add(linkBuilder.build(link));
		}
		
		// Relevance
		if (searchRequest.getQuery() == null) {
			// The effective query is MatchAllDocsQuery, relevance means nothing
		} else {
			addProperty(entry, QName.get("score", relevanceNamespace), result.getScore(), false);
		}
		
		// Content
		final Element content = DocumentHelper.createElement(QName.get("content", atomNamespace));
		entry.add(content);
		content.addAttribute("type", "xhtml");
		
		final Element div = DocumentHelper.createElement(QName.get("div", xhtmlNamespace));
		content.add(div);
		
		final Element dl = fieldListTransformer.transform(fields);
		div.add(dl);
	}

	public Namespace getAtomNamespace() {
		return atomNamespace;
	}

	public void setAtomNamespace(Namespace atomNamespace) {
		this.atomNamespace = atomNamespace;
	}

	public Namespace getOpenSearchNamespace() {
		return openSearchNamespace;
	}

	public void setOpenSearchNamespace(Namespace openSearchNamespace) {
		this.openSearchNamespace = openSearchNamespace;
	}

}
