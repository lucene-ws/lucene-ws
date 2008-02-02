package net.lucenews.test.support;

import java.util.List;

import javax.xml.xpath.XPathExpressionException;

import org.junit.Assert;
import org.w3c.dom.Element;

public class EntryAsserter {

	private DomUtility dom;
	
	public EntryAsserter() {
		this.dom = new DomUtility();
	}
	
	public void assertEntry(Element element) throws XPathExpressionException {
		Assert.assertEquals("entry tag name", "entry", element.getTagName());
		
		List<Element> contents = dom.elementsByPath(element, "./content");
		
		Assert.assertTrue("entry has no more than 1 content element", !(contents.size() > 1));
		
		// atom:entry elements MUST contain exactly one atom:id element.
		List<Element> ids = dom.elementsByPath(element, "./id");
		Assert.assertEquals("# of id elements", 1, ids.size());
		
		// atom:entry elements that contain no child atom:content element
		// MUST contain at least one atom:link element with a rel attribute
		// value of "alternate".
		if (contents.isEmpty()) {
			List<Element> alternateLinks = dom.elementsByPath(element, "./link[rel=alternate]");
			Assert.assertTrue("entry has at least one alternate link", !alternateLinks.isEmpty());
		}
		
		/**
		 * atom:entry elements MUST NOT contain more than one atom:link element
		 * with a rel attribute value of "alternate" that has the same
		 * combination of type and hreflang attribute values.
		 */
		// TODO
		
		/**
		 * atom:entry elements MAY contain additional atom:link elements beyond
		 * those described above.
		 */
		// TODO
		
		/**
		 * atom:entry elements MUST NOT contain more than one atom:published
		 * element.
		 */
		List<Element> publisheds = dom.elementsByPath(element, "./published");
		Assert.assertFalse("no more than atom:published element", publisheds.size() > 1);
		
		/**
		 * atom:entry elements MUST NOT contain more than one atom:rights
		 * element.
		 */
		List<Element> rights = dom.elementsByPath(element, "./rights");
		Assert.assertFalse("no more than atom:published element", rights.size() > 1);
		
		/**
		 * atom:entry elements MUST NOT contain more than one atom:published
		 * element.
		 */
		List<Element> sources = dom.elementsByPath(element, "./source");
		Assert.assertFalse("no more than atom:published element", sources.size() > 1);
		
		// TODO
		
		/**
		 * atom:entry elements MUST NOT contain more than one atom:summary
		 * element.
		 */
		List<Element> summaries = dom.elementsByPath(element, "./summary");
		Assert.assertFalse("no more than atom:summary element", summaries.size() > 1);
		
		/**
		 * atom:entry elements MUST contain exactly one atom:title element.
		 */
		List<Element> titles = dom.elementsByPath(element, "./title");
		Assert.assertEquals("entry has exactly one atom:title element", 1, titles.size());
		
		/**
		 * atom:entry elements MUST contain exactly one atom:updated element.
		 */
		List<Element> updateds = dom.elementsByPath(element, "./updated");
		Assert.assertEquals("entry has exactly one atom:updated element", 1, updateds.size());
	}
	
}
