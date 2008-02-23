package net.lucenews3.opensearch.dom4j;

import net.lucenews3.atom.dom4j.AbstractBuilder;
import net.lucenews3.opensearch.Query;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class QueryBuilder extends AbstractBuilder {

	public Element build(Query query) {
		final Element element = DocumentHelper.createElement("Query");
		build(query, element);
		return element;
	}
	
	public void build(Query query, Element element) {
		addAttribute(element, "role", query.getRole());
		addAttribute(element, "title", query.getTitle(), false);
		addAttribute(element, "totalResults", query.getTotalResults(), false);
		addAttribute(element, "searchTerms", query.getSearchTerms(), false);
		addAttribute(element, "count", query.getCount(), false);
		addAttribute(element, "startIndex", query.getStartIndex(), false);
		addAttribute(element, "startPage", query.getStartPage(), false);
		addAttribute(element, "language", query.getLanguage(), false);
		addAttribute(element, "inputEncoding", query.getInputEncoding(), false);
		addAttribute(element, "outputEncoding", query.getOutputEncoding(), false);
	}
	
}
