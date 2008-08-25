package net.lucenews3.test;

import static javax.xml.stream.XMLStreamReader.*;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import net.lucenews3.XMLStreamUtility;

import org.junit.Assert;

public class FeedParser {

	private String atomNamespaceURI = "http://www.w3.org/2005/Atom";

	public void parse(XMLStreamReader xml) throws XMLStreamException {
		Assert.assertEquals("start of feed element", START_ELEMENT, xml.nextTag());
		
		QName feedName = xml.getName();
		Assert.assertEquals("feed element namespace URI", atomNamespaceURI, feedName.getNamespaceURI());
		Assert.assertEquals("feed element name", "feed", feedName.getLocalPart());
		
		while (xml.nextTag() == START_ELEMENT) {
			QName name = xml.getName();
			if (atomNamespaceURI.equals(name.getNamespaceURI())) {
				String localPart = name.getLocalPart();
				if ("author".equals(localPart)) {
					
				} else if ("category".equals(localPart)) {
					
				} else if ("contributor".equals(localPart)) {
					
				} else if ("generator".equals(localPart)) {
					
				} else if ("icon".equals(localPart)) {
					
				} else if ("id".equals(localPart)) {
					
				} else if ("link".equals(localPart)) {
					
				} else if ("logo".equals(localPart)) {
					
				} else if ("rights".equals(localPart)) {
					
				} else if ("subtitle".equals(localPart)) {
					
				} else if ("title".equals(localPart)) {
					
				} else if ("updated".equals(localPart)) {
				
				} else if ("entry".equals(localPart)) {
					parseEntry(xml);
				} else {
					throw new RuntimeException("Unknown Atom feed child: \"" + localPart + "\"");
				}
			} else {
				// Ignore
				XMLStreamUtility.endElement(xml);
			}
		}
	}

	public void parseEntry(XMLStreamReader xml) throws XMLStreamException {
		while (xml.nextTag() == START_ELEMENT) {
			QName name = xml.getName();
			if (atomNamespaceURI.equals(name.getNamespaceURI())) {
				String localPart = name.getLocalPart();
				if ("author".equals(localPart)) {
					
				} else if ("category".equals(localPart)) {
					
				} else if ("contributor".equals(localPart)) {
					
				} else if ("generator".equals(localPart)) {
					
				} else if ("icon".equals(localPart)) {
					
				} else if ("id".equals(localPart)) {
					
				} else if ("link".equals(localPart)) {
					
				} else if ("logo".equals(localPart)) {
					
				} else if ("rights".equals(localPart)) {
					
				} else if ("subtitle".equals(localPart)) {
					
				} else if ("title".equals(localPart)) {
					
				} else if ("updated".equals(localPart)) {
				
				} else if ("entry".equals(localPart)) {
					parseEntry(xml);
				} else {
					throw new RuntimeException("Unknown Atom feed child: \"" + localPart + "\"");
				}
			} else {
				XMLStreamUtility.endElement(xml);
			}
		}
	}

}
