package net.lucenews.test;

import java.util.List;

import javax.xml.xpath.XPathExpressionException;

import org.junit.Assert;
import org.w3c.dom.Element;

public class WorkspaceAsserter {

	private DomUtility dom;
	private CollectionAsserter collectionAsserter;
	
	public WorkspaceAsserter() {
		this.dom = new DomUtility();
		this.collectionAsserter = new CollectionAsserter();
	}
	
	public void assertWorkspace(Element element) throws XPathExpressionException {
		Assert.assertEquals("Workspace tag name", "workspace", element.getTagName());
		Assert.assertNotNull("Workspace title", element.getAttribute("title"));
		
		List<Element> collectionElements = dom.elementsByPath(element, "./collection");
		for (Element collectionElement : collectionElements) {
			collectionAsserter.assertCollection(collectionElement);
		}
	}
	
}
