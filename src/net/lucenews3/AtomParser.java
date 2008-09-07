package net.lucenews3;

import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class AtomParser {

	protected final XMLStreamReader xml;
	protected String entryTitle;
	protected String entrySummary;
	protected String xoxoClass;
	protected String xoxoTerm;
	protected String xoxoDefinition;

	public AtomParser(XMLStreamReader xml) {
		this.xml = xml;
	}

	public void parse() throws XMLStreamException {
		if (xml.nextTag() == START_ELEMENT) {
			String localName = xml.getLocalName();
			if ("feed".equals(localName)) {
				parseFeed();
			} else if ("entry".equals(localName)) {
				parseEntry();
			} else {
				throw new XMLStreamException("Unknown element: " + xml.getName(), xml.getLocation());
			}
		}
	}

	public void parseFeed() throws XMLStreamException {
		while (xml.nextTag() == START_ELEMENT) {
			String localName = xml.getLocalName();
			if ("entry".equals(localName)) {
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
		String localName = xml.getLocalName();
		if ("title".equals(localName)) {
			parseEntryTitle();
		} else if ("summary".equals(localName)) {
			parseEntrySummary();
		} else if ("content".equals(localName)) {
			parseEntryContent();
		} else {
			XMLStreamUtility.endElement(xml);
		}
	}

	protected void parseEntryTitle() throws XMLStreamException {
		entryTitle = xml.getElementText();
	}

	protected void parseEntrySummary() throws XMLStreamException {
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
					String localName = xml.getLocalName();
					if ("div".equals(localName)) {
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
			String localName = xml.getLocalName();
			if ("dl".equals(localName)) {
				parseXOXOList();
			} else if ("ol".equals(localName)) {
				parseOrderedList();
			} else {
				XMLStreamUtility.endElement(xml);
			}
		}
	}

	protected void parseXOXOList() throws XMLStreamException {
		String previousLocalName = null;
		while (xml.nextTag() == START_ELEMENT) {
			String localName = xml.getLocalName();
			if ("dt".equals(localName)) {
				if (previousLocalName == null || previousLocalName.equals("dd")) {
					parseXOXOTerm();
				} else {
					throw new XMLStreamException("XOXO list out of order", xml.getLocation());
				}
				previousLocalName = localName;
			} else if ("dd".equals(localName)) {
				if (previousLocalName == null || previousLocalName.equals("dd")) {
					throw new XMLStreamException("XOXO list out of order", xml.getLocation());
				} else {
					parseXOXODefinition();
					parseXOXOTermDefinition();
				}
				previousLocalName = localName;
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
		xoxoDefinition = xml.getElementText();
	}

	protected void parseXOXOTermDefinition() throws XMLStreamException {
		
	}

	/**
	 * Cursor expected to be sitting immediately after encountering the start element of
	 * &lt;ol&gt;.
	 * 
	 * @throws XMLStreamException
	 */
	protected void parseOrderedList() throws XMLStreamException {
		while (xml.nextTag() == START_ELEMENT) {
			String localName = xml.getLocalName();
			if ("li".equals(localName)) {
				parseListItem();
			} else {
				XMLStreamUtility.endElement(xml);
			}
		}
	}

	/**
	 * Cursor expected to be immediately after the start of an &lt;li&gt; element.
	 * 
	 * @throws XMLStreamException
	 */
	protected void parseListItem() throws XMLStreamException {
		XMLStreamUtility.endElement(xml);
	}

}
