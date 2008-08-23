package net.lucenews3;

import static javax.xml.stream.XMLStreamReader.*;

import java.util.ArrayDeque;
import java.util.Deque;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class XMLStreamUtility {

	public static int startElement(XMLStreamReader xml, String localName) throws XMLStreamException {
		// TODO
		while (xml.nextTag() == START_ELEMENT && !localName.equals(xml.getLocalName()));
		return xml.getEventType();
	}

	public static int startElement(XMLStreamReader xml, QName name) throws XMLStreamException {
		// TODO
		while (xml.nextTag() == START_ELEMENT && !name.equals(xml.getName()));
		return xml.getEventType();
	}

	public static int endElement(XMLStreamReader xml) throws XMLStreamException {
		return endElement(xml, xml.getName());
	}

	public static int endElement(XMLStreamReader xml, QName name) throws XMLStreamException {
		Deque<QName> path = new ArrayDeque<QName>(1);
		path.addLast(name);
		
		while (!path.isEmpty()) {
			switch (xml.next()) {
			case START_ELEMENT:
				path.addLast(xml.getName());
				break;
			case END_ELEMENT:
				QName currentName = xml.getName();
				QName expectedName = path.getLast();
				if (currentName.equals(expectedName)) {
					path.removeLast();
				} else {
					throw new XMLStreamException("Expected end of element " + expectedName + ", encountered end of " + currentName);
				}
				break;
			default:
				break;
			}
		}
		
		return xml.getEventType();
	}

}
