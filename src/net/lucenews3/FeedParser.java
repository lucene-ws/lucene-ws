package net.lucenews3;

import static javax.xml.stream.XMLStreamReader.*;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class FeedParser {

	private XMLStreamReader xml;

	public void parse() throws XMLStreamException {
		if (xml.nextTag() == START_ELEMENT) {
			QName name = xml.getName();
			if ("feed".equals(name.getLocalPart())) {
				parseFeed();
			} else if ("entry".equals(name.getLocalPart())) {
				parseEntry();
			} else {
				throw new XMLStreamException("Unknown element: " + name, xml.getLocation());
			}
		}
	}

	public void parseFeed() {
		
	}

	public void parseEntry() {
		
	}

}
