package net.lucenews.test;

import java.util.List;

import javax.xml.xpath.XPathExpressionException;

import org.junit.Assert;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class IntrospectionDocumentAsserter {

	private DomUtility dom;
	private WorkspaceAsserter workspaceAsserter;
	
	public IntrospectionDocumentAsserter() {
		this.dom = new DomUtility();
		this.workspaceAsserter = new WorkspaceAsserter();
	}
	
	public void assertIntrospectionDocument(Document document) throws XPathExpressionException {
		Element root = document.getDocumentElement();
		Assert.assertEquals("Introspection document tag name", "service", root.getTagName());
		// TODO Assert.assertEquals("Introspection document namespace", "http://purl.org/atom/app#", root.getAttribute("xmlns"));
		
		List<Element> workspaceElements = dom.elementsByPath(document, "/service/workspace");
		for (Element workspaceElement : workspaceElements) {
			workspaceAsserter.assertWorkspace(workspaceElement);
		}
	}
	
}
