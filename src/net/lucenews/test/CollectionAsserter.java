package net.lucenews.test;

import org.junit.Assert;
import org.w3c.dom.Element;

public class CollectionAsserter {

	//private DomUtility dom;
	
	public CollectionAsserter() {
		//this.dom = new DomUtility();
	}
	
	public void assertCollection(Element element) {
		Assert.assertEquals("Collection tag name", "collection", element.getTagName());
		Assert.assertNotNull("Collection title", element.getAttribute("title"));
	}
	
}
