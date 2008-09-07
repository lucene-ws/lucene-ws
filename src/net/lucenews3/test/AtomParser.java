package net.lucenews3.test;

import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

import java.io.File;
import java.io.FileReader;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import net.lucenews3.XMLStreamUtility;

public class AtomParser {

	private Logger logger;
	private String atomNamespaceURI  = "http://www.w3.org/2005/Atom";
	private String xhtmlNamespaceURI = "http://www.w3.org/1999/xhtml";
	private String xmlNamespaceURI   = "http://www.w3.org/XML/1998/namespace";

	public static void main(String... args) throws Exception {
		BasicConfigurator.configure();
		
		XMLInputFactory factory = XMLInputFactory.newInstance();
		
		XMLStreamReader xml;
		if (args.length > 0) {
			String fileName = args[0];
			File file = new File(fileName);
			FileReader fileReader = new FileReader(file);
			xml = factory.createXMLStreamReader(fileReader);
		} else {
			// Read XML from standard input
			xml = factory.createXMLStreamReader(System.in);
		}
		
		AtomParser atomParser = new AtomParser();
		atomParser.parse(xml);
	}

	public AtomParser() {
		this.logger = Logger.getLogger(getClass());
	}

	public void parse(XMLStreamReader xml) throws XMLStreamException {
		if (xml.nextTag() == START_ELEMENT) {
			parseFeedOrEntry(xml);
		} else {
			throw new XMLStreamException("Expected start-of-element", xml.getLocation());
		}
	}

	public void parseFeedOrEntry(XMLStreamReader xml) throws XMLStreamException {
		String namespaceURI = xml.getNamespaceURI();
		
		if (namespaceURI == null) {
			throw new XMLStreamException("No namespace URI", xml.getLocation());
		}
		
		if (namespaceURI.equals(atomNamespaceURI)) {
			String localName = xml.getLocalName();
			if ("feed".equals(localName)) {
				parseFeed(xml);
			} else if ("entry".equals(localName)) {
				parseEntry(xml);
			} else {
				throw new XMLStreamException("Expected Atom feed or Atom entry element, found " + xml.getName(), xml.getLocation());
			}
		} else {
			throw new XMLStreamException("Expected Atom element", xml.getLocation());
		}
	}

	public void parseFeed(XMLStreamReader xml) throws XMLStreamException {
		logger.debug("Parsing Atom feed");
		
		int authorCount = 0;
		int generatorCount = 0;
		int iconCount = 0;
		int idCount = 0;
		int logoCount = 0;
		int rightsCount = 0;
		int subtitleCount = 0;
		int titleCount = 0;
		int updatedCount = 0;
		
		while (xml.nextTag() == START_ELEMENT) {
			String namespaceURI = xml.getNamespaceURI();
			if (namespaceURI.equals(atomNamespaceURI)) {
				String localPart = xml.getLocalName();
				if ("author".equals(localPart)) {
					authorCount++;
					parseAuthor(xml);
				} else if ("category".equals(localPart)) {
					parseCategory(xml);
				} else if ("contributor".equals(localPart)) {
					parseContributor(xml);
				} else if ("generator".equals(localPart)) {
					generatorCount++;
					if (generatorCount > 1) {
						throw new XMLStreamException("Multiple Atom generator elements encountered", xml.getLocation());
					}
					parseGenerator(xml);
				} else if ("icon".equals(localPart)) {
					iconCount++;
					if (iconCount > 1) {
						throw new XMLStreamException("Multiple Atom icon elements encountered", xml.getLocation());
					}
					parseIcon(xml);
				} else if ("id".equals(localPart)) {
					idCount++;
					if (idCount > 1) {
						throw new XMLStreamException("Multiple Atom id elements encountered", xml.getLocation());
					}
					parseID(xml);
				} else if ("link".equals(localPart)) {
					parseLink(xml);
				} else if ("logo".equals(localPart)) {
					logoCount++;
					if (logoCount > 1) {
						throw new XMLStreamException("Multiple Atom logo elements encountered", xml.getLocation());
					}
					parseLogo(xml);
				} else if ("rights".equals(localPart)) {
					rightsCount++;
					if (rightsCount > 1) {
						throw new XMLStreamException("Multiple Atom rights elements encountered", xml.getLocation());
					}
					parseRights(xml);
				} else if ("subtitle".equals(localPart)) {
					subtitleCount++;
					if (subtitleCount > 1) {
						throw new XMLStreamException("Multiple Atom subtitle elements encountered", xml.getLocation());
					}
					parseSubtitle(xml);
				} else if ("title".equals(localPart)) {
					titleCount++;
					if (titleCount > 1) {
						throw new XMLStreamException("Multiple Atom title elements encountered", xml.getLocation());
					}
					parseTitle(xml);
				} else if ("updated".equals(localPart)) {
					updatedCount++;
					if (updatedCount > 1) {
						throw new XMLStreamException("Multiple Atom updated elements encountered", xml.getLocation());
					}
					parseUpdated(xml);
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
		
		if (idCount == 0) {
			throw new XMLStreamException("No Atom id element encountered", xml.getLocation());
		}
		
		if (titleCount == 0) {
			throw new XMLStreamException("No Atom title element encountered", xml.getLocation());
		}
		
		if (updatedCount == 0) {
			throw new XMLStreamException("No Atom updated element encountered", xml.getLocation());
		}
	}

	public void parseEntry(XMLStreamReader xml) throws XMLStreamException {
		logger.debug("Parsing Atom entry");
		
		int contentCount = 0;
		int idCount = 0;
		int publishedCount = 0;
		int rightsCount = 0;
		int sourceCount = 0;
		int summaryCount = 0;
		int titleCount = 0;
		int updatedCount = 0;
		
		while (xml.nextTag() == START_ELEMENT) {
			String namespaceURI = xml.getNamespaceURI();
			if (namespaceURI.equals(atomNamespaceURI)) {
				String localPart = xml.getLocalName();
				if ("author".equals(localPart)) {
					parseAuthor(xml);
				} else if ("category".equals(localPart)) {
					parseCategory(xml);
				} else if ("content".equals(localPart)) {
					contentCount++;
					if (contentCount > 1) {
						throw new XMLStreamException("Multiple Atom content elements encountered", xml.getLocation());
					}
					parseContent(xml);
				} else if ("contributor".equals(localPart)) {
					parseContributor(xml);
				} else if ("id".equals(localPart)) {
					idCount++;
					if (idCount > 1) {
						throw new XMLStreamException("Multiple Atom id elements encountered", xml.getLocation());
					}
					parseID(xml);
				} else if ("link".equals(localPart)) {
					parseLink(xml);
				} else if ("published".equals(localPart)) {
					publishedCount++;
					if (publishedCount > 1) {
						throw new XMLStreamException("Multiple Atom published elements encountered", xml.getLocation());
					}
					parsePublished(xml);
				} else if ("rights".equals(localPart)) {
					rightsCount++;
					if (rightsCount > 1) {
						throw new XMLStreamException("Multiple Atom rights elements encountered", xml.getLocation());
					}
					parseRights(xml);
				} else if ("source".equals(localPart)) {
					sourceCount++;
					if (sourceCount > 1) {
						throw new XMLStreamException("Multiple Atom source elements encountered", xml.getLocation());
					}
					parseSource(xml);
				} else if ("summary".equals(localPart)) {
					summaryCount++;
					if (summaryCount > 1) {
						throw new XMLStreamException("Multiple Atom summary elements encountered", xml.getLocation());
					}
					parseSummary(xml);
				} else if ("title".equals(localPart)) {
					titleCount++;
					if (titleCount > 1) {
						throw new XMLStreamException("Multiple Atom title elements encountered", xml.getLocation());
					}
					parseTitle(xml);
				} else if ("updated".equals(localPart)) {
					updatedCount++;
					if (updatedCount > 1) {
						throw new XMLStreamException("Multiple Atom updated elements encountered", xml.getLocation());
					}
					parseUpdated(xml);
				} else {
					throw new RuntimeException("Unknown Atom entry child: \"" + localPart + "\"");
				}
			} else {
				XMLStreamUtility.endElement(xml);
			}
		}
		
		if (idCount == 0) {
			throw new XMLStreamException("No Atom id element encountered", xml.getLocation());
		}
		
		if (titleCount == 0) {
			throw new XMLStreamException("No Atom title element encountered", xml.getLocation());
		}
		
		if (updatedCount == 0) {
			throw new XMLStreamException("No Atom updated element encountered", xml.getLocation());
		}
	}

	public void parseCommonAttributes(XMLStreamReader xml) throws XMLStreamException {
		int attributeCount = xml.getAttributeCount();
		for (int index = 0; index < attributeCount; index++) {
			String namespace = xml.getAttributeNamespace(index);
			if (namespace == null) {
			} else if (namespace.equals(atomNamespaceURI)) {
				String localName = xml.getAttributeLocalName(index);
				
			} else {
				continue;
			}
		}
	}

	public boolean isCommonAttribute(XMLStreamReader xml, int index) throws XMLStreamException {
		String namespaceURI = xml.getAttributeNamespace(index);
		
		if (namespaceURI.equals(xmlNamespaceURI)) {
			String localName = xml.getAttributeLocalName(index);
			if ("base".equals(localName)) {
				return true;
			} else if ("lang".equals(localName)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public void parseText(XMLStreamReader xml) throws XMLStreamException {
		parseCommonAttributes(xml);
		
		String type = null;
		
		int attributeCount = xml.getAttributeCount();
		for (int index = 0; index < attributeCount; index++) {
			String namespaceURI = xml.getAttributeNamespace(index);
			if (namespaceURI.equals(atomNamespaceURI)) {
				String localName = xml.getLocalName();
				if ("type".equals(localName)) {
					type = xml.getAttributeValue(index);
				} else {
					throw new XMLStreamException("Unknown Atom text attribute: " + xml.getAttributeName(index), xml.getLocation());
				}
			}
		}
		
		logger.debug("Parsing text of type \"" + type + "\"");
		if (type == null || "text".equals(type) || "html".equals(type)) {
			parsePlainText(xml);
		} else if ("xhtml".equals(type)) {
			parseXHTMLText(xml);
		} else {
			throw new XMLStreamException("Unknown Atom text type: \"" + type + "\"", xml.getLocation());
		}
	}

	public void parsePlainText(XMLStreamReader xml) throws XMLStreamException {
		String text = xml.getElementText();
		logger.debug("Parsed plain text: \"" + text + "\"");
	}

	public void parseXHTMLText(XMLStreamReader xml) throws XMLStreamException {
		while (xml.nextTag() == START_ELEMENT) {
			String namespaceURI = xml.getNamespaceURI();
			if (namespaceURI.equals(atomNamespaceURI)) {
				throw new XMLStreamException("Atom element cannot exist within Atom XHTML text construct: " + xml.getName(), xml.getLocation());
			} else if (namespaceURI.equals(xhtmlNamespaceURI)) {
				String localName = xml.getLocalName();
				if ("div".equals(localName)) {
					XMLStreamUtility.endElement(xml);
				} else {
					throw new XMLStreamException("Unknown XHTML element within Atom XHTML text construct: " + xml.getName(), xml.getLocation());
				}
			} else {
				XMLStreamUtility.endElement(xml);
			}
		}
	}

	public void parsePerson(XMLStreamReader xml) throws XMLStreamException {
		parseCommonAttributes(xml);
		
		while (xml.nextTag() == START_ELEMENT) {
			String namespaceURI = xml.getNamespaceURI();
			if (namespaceURI.equals(atomNamespaceURI)) {
				String localName = xml.getLocalName();
				if ("name".equals(localName)) {
					String name = xml.getElementText();
				} else if ("uri".equals(localName)) {
					String uri = xml.getElementText();
				} else if ("email".equals(localName)) {
					String email = xml.getElementText();
				} else {
					throw new XMLStreamException("Unknown Atom person element: " + xml.getName(), xml.getLocation());
				}
			} else {
				XMLStreamUtility.endElement(xml);
			}
		}
	}

	public void parseAuthor(XMLStreamReader xml) throws XMLStreamException {
		parsePerson(xml);
	}

	public void parseDate(XMLStreamReader xml) throws XMLStreamException {
		parseCommonAttributes(xml);
		
		String date = xml.getElementText();
		
		// TODO Validate date format
	}

	public void parseContent(XMLStreamReader xml) throws XMLStreamException {
		String type = null;
		String src = null;
		
		int attributeCount = xml.getAttributeCount();
		for (int index = 0; index < attributeCount; index++) {
			String namespaceURI = xml.getAttributeNamespace(index);
			if (namespaceURI.equals(atomNamespaceURI)) {
				String localName = xml.getAttributeLocalName(index);
				if ("type".equals(localName)) {
					type = xml.getAttributeValue(index);
				} else if ("src".equals(localName)) {
					src = xml.getAttributeValue(index);
				}
			}
		}
		
		if (type == null) {
			throw new XMLStreamException("Atom content element contains no \"type\" attribute", xml.getLocation());
		} else {
			if ("text".equals(type) || "html".equals(type)) {
				parseInlineTextContext(xml);
			} else if ("xhtml".equals(type)) {
				parseInlineXHTMLContext(xml);
			} else if (isMediaType(type)) {
				if (src == null) {
					XMLStreamUtility.endElement(xml);
				} else {
					if (isUri(src)) {
						String text = xml.getElementText();
						if (text == null || text.equals("")) {
							// Great
						} else {
							throw new XMLStreamException("Out-of-line Atom content cannot contain text", xml.getLocation());
						}
					} else {
						throw new XMLStreamException("Atom content src URI is invalid: \"" + src + "\"", xml.getLocation());
					}
				}
			} else {
				throw new XMLStreamException("Unknown Atom content type: \"" + type + "\"", xml.getLocation());
			}
		}
	}

	public boolean isUri(String string) {
		return true;
	}

	public boolean isNCName(String string) {
		return string.matches("[^:]+");
	}

	public boolean isMediaType(String type) {
		return type.matches(".+/.+");
	}

	public boolean isLanguageTag(String string) {
		return string.matches("[A-Za-z]{1,8}(-[A-Za-z0-9]{1,8})*");
	}

	public void parseInlineTextContext(XMLStreamReader xml) throws XMLStreamException {
		parseCommonAttributes(xml);
		
		String text = xml.getElementText();
	}

	public void parseInlineXHTMLContext(XMLStreamReader xml) throws XMLStreamException {
		parseCommonAttributes(xml);

		while (xml.nextTag() == START_ELEMENT) {
			String namespaceURI = xml.getNamespaceURI();
			if (namespaceURI.equals(atomNamespaceURI)) {
				throw new XMLStreamException("Atom element cannot exist within Atom inline XHTML content construct: " + xml.getName(), xml.getLocation());
			} else if (namespaceURI.equals(xhtmlNamespaceURI)) {
				String localName = xml.getLocalName();
				if ("div".equals(localName)) {
					XMLStreamUtility.endElement(xml);
				} else {
					throw new XMLStreamException("Unknown XHTML element within Atom inline XHTML content construct: " + xml.getName(), xml.getLocation());
				}
			} else {
				XMLStreamUtility.endElement(xml);
			}
		}
	}

	public void parseCategory(XMLStreamReader xml) throws XMLStreamException {
		parseCommonAttributes(xml);
		
		String term = null;
		String scheme = null;
		String label = null;
		
		int attributeCount = xml.getAttributeCount();
		for (int index = 0; index < attributeCount; index++) {
			String namespaceURI = xml.getAttributeNamespace(index);
			if (namespaceURI.equals(atomNamespaceURI)) {
				String localName = xml.getAttributeLocalName(index);
				if ("term".equals(localName)) {
					term = xml.getAttributeValue(index);
				} else if ("scheme".equals(localName)) {
					scheme = xml.getAttributeValue(index);
				} else if ("label".equals(localName)) {
					label = xml.getAttributeValue(index);
				} else {
					throw new XMLStreamException("Unknown Atom category attribute: " + xml.getAttributeName(index), xml.getLocation());
				}
			}
		}
		
		if (term == null) {
			throw new XMLStreamException("Atom category requires a \"term\" attribute", xml.getLocation());
		}
		
		if (scheme != null && !isUri(scheme)) {
			throw new XMLStreamException("Atom category \"scheme\" attribute must be a valid URI: \"" + scheme + "\"", xml.getLocation());
		}
	}

	public void parseContributor(XMLStreamReader xml) throws XMLStreamException {
		parsePerson(xml);
	}

	public void parseGenerator(XMLStreamReader xml) throws XMLStreamException {
		parseCommonAttributes(xml);
		
		String uri = null;
		String version = null;
		
		int attributeCount = xml.getAttributeCount();
		for (int index = 0; index < attributeCount; index++) {
			String namespaceURI = xml.getAttributeNamespace(index);
			if (namespaceURI.equals(atomNamespaceURI)) {
				String localName = xml.getAttributeLocalName(index);
				if ("uri".equals(localName)) {
					uri = xml.getAttributeValue(index);
				} else if ("version".equals(localName)) {
					version = xml.getAttributeValue(index);
				} else {
					throw new XMLStreamException("Unknown Atom generator attribute: " + xml.getAttributeName(index), xml.getLocation());
				}
			}
		}
		
		if (uri != null && !isUri(uri)) {
			throw new XMLStreamException("Atom generator \"uri\" attribute must be a valid URI: \"" + uri + "\"", xml.getLocation());
		}
		
		String text = xml.getElementText();
	}

	public void parseIcon(XMLStreamReader xml) throws XMLStreamException {
		parseURIElement(xml, "icon");
	}

	public void parseID(XMLStreamReader xml) throws XMLStreamException {
		parseCommonAttributes(xml);
		
		String uri = xml.getElementText();
		
		if (uri == null || uri.equals("")) {
			throw new XMLStreamException("Atom id element requires a URI in its content", xml.getLocation());
		}
		
		if (!isUri(uri)) {
			throw new XMLStreamException("Atom id content must be a valid URI: \"" + uri + "\"", xml.getLocation());
		}
	}

	public void parseLink(XMLStreamReader xml) throws XMLStreamException {
		parseCommonAttributes(xml);
		
		String href = null;
		String rel = null;
		String type = null;
		String hreflang = null;
		String title = null;
		String length = null;
		
		int attributeCount = xml.getAttributeCount();
		for (int index = 0; index < attributeCount; index++) {
			String namespaceURI = xml.getAttributeNamespace(index);
			if (namespaceURI == null) {
				namespaceURI = xml.getNamespaceURI();
			}
			if (namespaceURI == null) {
				
			} else if (namespaceURI.equals(atomNamespaceURI)) {
				String localName = xml.getAttributeLocalName(index);
				if ("href".equals(localName)) {
					href = xml.getAttributeValue(index);
				} else if ("rel".equals(localName)) {
					rel = xml.getAttributeValue(index);
				} else if ("type".equals(localName)) {
					type = xml.getAttributeLocalName(index);
				} else if ("hreflang".equals(localName)) {
					hreflang = xml.getAttributeValue(index);
				} else if ("title".equals(localName)) {
					title = xml.getAttributeValue(index);
				} else if ("length".equals(localName)) {
					length = xml.getAttributeValue(index);
				} else {
					throw new XMLStreamException("Unknown Atom link attribute: " + xml.getAttributeName(index), xml.getLocation());
				}
			}
		}
		
		if (xml.nextTag() == END_ELEMENT) {
			// Good
		} else {
			throw new XMLStreamException("Expected end-of-element", xml.getLocation());
		}
		
		if (href == null) {
			throw new XMLStreamException("Atom link requires \"href\" attribute", xml.getLocation());
		}
		
		if (!isUri(href)) {
			throw new XMLStreamException("Atom link \"href\" attribute must be a valid URI: \"" + href + "\"", xml.getLocation());
		}
		
		if (rel != null && !isNCName(rel) && !isUri(rel)) {
			throw new XMLStreamException("Atom link \"rel\" attribute must be either an NCName or a URI: \"" + rel + "\"", xml.getLocation());
		}
		
		if (type != null && !isMediaType(type)) {
			throw new XMLStreamException("Atom link \"type\" attribute must be a valid media type: \"" + type + "\"", xml.getLocation());
		}
		
		if (hreflang != null && !isLanguageTag(hreflang)) {
			throw new XMLStreamException("Atom link \"hreflang\" attribute must be a valid langage tag: \"" + hreflang + "\"", xml.getLocation());
		}
		
		// Title is optional and unrestricted
		
		// Length is optional and unrestricted
	}

	public void parseLogo(XMLStreamReader xml) throws XMLStreamException {
		parseURIElement(xml, "logo");
	}

	public void parseURIElement(XMLStreamReader xml, String localName) throws XMLStreamException {
		parseCommonAttributes(xml);
		
		String uri = xml.getElementText();
		
		if (uri == null || uri.equals("")) {
			throw new XMLStreamException("Atom " + localName + " element requires a URI in its content", xml.getLocation());
		}
		
		if (!isUri(uri)) {
			throw new XMLStreamException("Atom " + localName + " content must be a valid URI: \"" + uri + "\"", xml.getLocation());
		}
	}

	public void parsePublished(XMLStreamReader xml) throws XMLStreamException {
		parseDate(xml);
	}

	public void parseRights(XMLStreamReader xml) throws XMLStreamException {
		parseText(xml);
	}

	public void parseSource(XMLStreamReader xml) throws XMLStreamException {
		parseCommonAttributes(xml);
		
		int generatorCount = 0;
		int iconCount = 0;
		int idCount = 0;
		int logoCount = 0;
		int rightsCount = 0;
		int subtitleCount = 0;
		int titleCount = 0;
		int updatedCount = 0;
		
		while (xml.nextTag() == START_ELEMENT) {
			String namespaceURI = xml.getNamespaceURI();
			if (namespaceURI.equals(atomNamespaceURI)) {
				String localName = xml.getLocalName();
				if ("author".equals(localName)) {
					parseAuthor(xml);
				} else if ("category".equals(localName)) {
					parseCategory(xml);
				} else if ("contributor".equals(localName)) {
					parseContributor(xml);
				} else if ("generator".equals(localName)) {
					generatorCount++;
					if (generatorCount > 1) {
						throw new XMLStreamException("Multiple Atom generator elements encountered", xml.getLocation());
					}
					parseGenerator(xml);
				} else if ("icon".equals(localName)) {
					iconCount++;
					if (iconCount > 1) {
						throw new XMLStreamException("Multiple Atom icon elements encountered", xml.getLocation());
					}
					parseIcon(xml);
				} else if ("id".equals(localName)) {
					idCount++;
					if (idCount > 1) {
						throw new XMLStreamException("Multiple Atom id elements encountered", xml.getLocation());
					}
					parseID(xml);
				} else if ("link".equals(localName)) {
					parseLink(xml);
				} else if ("logo".equals(localName)) {
					logoCount++;
					if (logoCount > 1) {
						throw new XMLStreamException("Multiple Atom logo elements encountered", xml.getLocation());
					}
					parseLogo(xml);
				} else if ("rights".equals(localName)) {
					rightsCount++;
					if (rightsCount > 1) {
						throw new XMLStreamException("Multiple Atom rights elements encountered", xml.getLocation());
					}
					parseRights(xml);
				} else if ("subtitle".equals(localName)) {
					subtitleCount++;
					if (subtitleCount > 1) {
						throw new XMLStreamException("Multiple Atom subtitle elements encountered", xml.getLocation());
					}
					parseSubtitle(xml);
				} else if ("title".equals(localName)) {
					titleCount++;
					if (titleCount > 1) {
						throw new XMLStreamException("Multiple Atom title elements encountered", xml.getLocation());
					}
					parseTitle(xml);
				} else if ("updated".equals(localName)) {
					updatedCount++;
					if (updatedCount > 1) {
						throw new XMLStreamException("Multiple Atom updated elements encountered", xml.getLocation());
					}
					parseUpdated(xml);
				} else {
					throw new XMLStreamException("Unknown Atom source child element: " + xml.getName(), xml.getLocation());
				}
			}
		}
	}

	public void parseSubtitle(XMLStreamReader xml) throws XMLStreamException {
		parseText(xml);
	}

	public void parseSummary(XMLStreamReader xml) throws XMLStreamException {
		parseText(xml);
	}

	public void parseTitle(XMLStreamReader xml) throws XMLStreamException {
		parseText(xml);
	}

	public void parseUpdated(XMLStreamReader xml) throws XMLStreamException {
		parseDate(xml);
	}

}
