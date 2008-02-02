package net.lucenews.test;

import org.junit.Assert;
import org.w3c.dom.Element;

public class FeedAsserter {

	public void assertFeed(Element element) {
		Assert.assertEquals("feed tag name", "feed", element.getTagName());
	}
	
}
