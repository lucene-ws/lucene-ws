package net.lucenews.test;

import java.util.List;

import org.junit.Assert;
import org.w3c.dom.Element;

public class XoxoAsserter {

	private DomUtility dom;
	
	public XoxoAsserter() {
		dom = new DomUtility();
	}
	
	public void assertXoxoDl(Element element) {
		Assert.assertEquals("xoxo tag name", "dl", element.getTagName());
		Assert.assertEquals("xoxo dl class", "xoxo", element.getAttribute("xoxo"));
		List<Element> childNodes = dom.elementsByPath(element, "./*");
		boolean isKey = true;
		for (Element childNode : childNodes) {
			if (isKey) {
				Assert.assertEquals("dt tag name", "dt", childNode.getTagName());
			} else {
				Assert.assertEquals("dd tag name", "dd", childNode.getTagName());
			}
			isKey = !isKey;
		}
	}
	
}
