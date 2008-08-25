package net.lucenews3.client;

import static javax.xml.stream.XMLStreamReader.*;

import java.util.Map.Entry;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import net.lucenews3.XMLStreamUtility;

import org.apache.lucene.document.Document;

public class Client {

	private String url;

	public Client(String url) {
		this.url = url;
	}

	public void createIndex(String name) throws XMLStreamException {
		createIndex(name, (Properties) null);
	}

	public void createIndex(String name, Properties properties) throws XMLStreamException {
		XMLStreamWriter xml = null;
		xml.writeStartDocument();
		xml.writeStartElement("entry");
		xml.writeDefaultNamespace("http://www.w3.org/2005/Atom");
		
		xml.writeStartElement("title");
		xml.writeCharacters(name);
		xml.writeEndElement();
		
		xml.writeStartElement("content");
		xml.writeAttribute("type", "xhtml");
		xml.writeStartElement("div");
		xml.writeDefaultNamespace("http://www.w3.org/1999/xhtml");
		if (properties == null) {
			xml.writeEmptyElement("dl");
		} else {
			xml.writeStartElement("dl");
			for (Entry<Object, Object> entry : properties.entrySet()) {
				Object key = entry.getKey();
				if (key == null) {
					xml.writeEmptyElement("dt");
				} else {
					xml.writeStartElement("dt");
					xml.writeCharacters(String.valueOf(key));
					xml.writeEndElement();
				}
				
				Object value = entry.getValue();
				if (value == null) {
					xml.writeEmptyElement("dd");
				} else {
					xml.writeStartElement("dd");
					xml.writeCharacters(String.valueOf(value));
					xml.writeEndElement();
				}
			}
			xml.writeEndElement(); // dl
		}
		
		xml.writeEndElement(); // div
		xml.writeEndElement(); // content
		xml.writeEndElement(); // entry
		xml.writeEndDocument();
		
		XMLStreamReader xmlr = null;
	}

	public Properties getServiceProperties() throws XMLStreamException {
		Properties properties = new Properties();
		
		XMLStreamReader xml = null;
		if (xml.nextTag() == START_ELEMENT) {
			while (xml.nextTag() == START_ELEMENT) {
				QName name = xml.getName();
				if ("content".equals(name.getLocalPart())) {
					while (xml.nextTag() == START_ELEMENT) {
						QName divName = xml.getName();
						if ("div".equals(divName.getLocalPart())) {
							while (xml.nextTag() == START_ELEMENT) {
								QName dlName = xml.getName();
								if ("dl".equals(dlName.getLocalPart())) {
									String key = null;
									String value = null;
									while (xml.nextTag() == START_ELEMENT) {
										QName dName = xml.getName();
										if ("dt".equals(dName.getLocalPart())) {
											key = xml.getElementText();
										} else if ("dd".equals(dName.getLocalPart())) {
											value = xml.getElementText();
											properties.put(key, value);
										} else {
											XMLStreamUtility.endElement(xml);
										}
									}
								} else {
									XMLStreamUtility.endElement(xml);
								}
							}
						} else {
							XMLStreamUtility.endElement(xml);
						}
					}
				} else {
					XMLStreamUtility.endElement(xml);
				}
			}
		}
		
		return properties;
	}

	public void createIndexProperties(String index, Properties properties) {
		
	}

	public List<String> getIndexes() throws XMLStreamException {
		List<String> indexes = new ArrayList<String>();
		
		XMLStreamReader xml = null;
		if (xml.nextTag() == START_ELEMENT) {
			QName serviceName = xml.getName();
			if ("service".equals(serviceName.getLocalPart())) {
				while (xml.nextTag() == START_ELEMENT) {
					QName collectionName = xml.getName();
					if ("collection".equals(collectionName)) {
						String href = null;
						String title = null;
						
						int attributeCount = xml.getAttributeCount();
						for (int i = 0; i < attributeCount; i++) {
							if ("href".equals(xml.getAttributeLocalName(i))) {
								href = xml.getAttributeValue(i);
							} else if ("title".equals(xml.getAttributeLocalName(i))) {
								title = xml.getAttributeValue(i);
							}
						}
						
						indexes.add(href);
					} else {
						XMLStreamUtility.endElement(xml);
					}
				}
			} else {
				XMLStreamUtility.endElement(xml);
			}
		}
		
		return indexes;
	}

	public void updateIndexProperties(String index, Properties properties) {
		
	}

	public void createDocument(String index, Document document) {
		
	}

}
