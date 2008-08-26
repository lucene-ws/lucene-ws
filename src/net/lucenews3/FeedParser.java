package net.lucenews3;

import static javax.xml.stream.XMLStreamReader.*;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class FeedParser {

	private XMLStreamReader xml;
	protected String entryTitle;

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

	public void parseFeed() throws XMLStreamException {
		while (xml.nextTag() == START_ELEMENT) {
			QName name = xml.getName();
			if ("entry".equals(name.getLocalPart())) {
				parseEntry();
			} else {
				XMLStreamUtility.endElement(xml);
			}
		}
	}

	public void parseEntry() throws XMLStreamException {
		while (xml.nextTag() == START_ELEMENT) {
			parseEntryChild();
		}
	}

	/**
	 * Parses any elements located immediately below an <entry...> element.
	 * 
	 * @throws XMLStreamException
	 */
	protected void parseEntryChild() throws XMLStreamException {
		QName name = xml.getName();
		if ("title".equals(name.getLocalPart())) {
			parseEntryTitle();
		} else if ("content".equals(name.getLocalPart())) {
			parseEntryContent();
		} else {
			XMLStreamUtility.endElement(xml);
		}
	}

	protected void parseEntryTitle() throws XMLStreamException {
		entryTitle = xml.getElementText();
	}

	protected void parseEntryContent() throws XMLStreamException {
		String type = null;
		
		int attributeCount = xml.getAttributeCount();
		for (int i = 0; i < attributeCount; i++) {
			String attributeLocalName = xml.getAttributeLocalName(i);
			if ("type".equals(attributeLocalName)) {
				type = xml.getAttributeValue(i);
				break;
			}
		}
		
		if (type == null) {
			XMLStreamUtility.endElement(xml);
		} else {
			if ("xhtml".equals(type)) {
				while (xml.nextTag() == START_ELEMENT) {
					QName name = xml.getName();
					if ("div".equals(name.getLocalPart())) {
						parseEntryXHTMLContent();
					} else {
						XMLStreamUtility.endElement(xml);
					}
				}
				parseEntryXHTMLContent();
			} else {
				XMLStreamUtility.endElement(xml);
			}
		}
	}

	/**
	 * Cursor is expected to be located immediately after the starting of the
	 * <div...> element nested immediately inside the <content...> element.
	 * 
	 * @throws XMLStreamException
	 */
	protected void parseEntryXHTMLContent() throws XMLStreamException {
		while (xml.nextTag() == START_ELEMENT) {
			QName name = xml.getName();
			if ("dl".equals(name.getLocalPart())) {
				parseXOXOList();
			} else {
				XMLStreamUtility.endElement(xml);
			}
		}
	}

	protected String xoxoClass;
	protected String xoxoTerm;
	protected String xoxoDefinition;

	protected void parseXOXOList() throws XMLStreamException {
		while (xml.nextTag() == START_ELEMENT) {
			QName name = xml.getName();
			if ("dt".equals(name.getLocalPart())) {
				
			} else if ("dd".equals(name.getLocalPart())) {
				
			} else {
				XMLStreamUtility.endElement(xml);
			}
		}
	}

	protected void parseXOXOTerm() throws XMLStreamException {
		xoxoClass = null;
		
		int attributeCount = xml.getAttributeCount();
		for (int i = 0; i < attributeCount; i++) {
			String attributeLocalName = xml.getAttributeLocalName(i);
			if ("class".equals(attributeLocalName)) {
				xoxoClass = xml.getAttributeValue(i);
				break;
			}
		}
		
		xoxoTerm = xml.getElementText();
		xoxoDefinition = null;
	}

	protected void parseXOXODefinition() throws XMLStreamException {
		
	}

}
