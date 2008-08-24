package net.lucenews3;

import static javax.xml.stream.XMLStreamReader.*;

import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class PrettyXMLStreamWriter implements XMLStreamWriter {

	private final XMLStreamWriter target;
	private int lastEventType;
	private int depth;
	private String tab = "\t";
	private String endOfLine = "\n";

	public static void main(String[] args) throws Exception {
		XMLStreamWriter writer = XMLOutputFactory.newInstance().createXMLStreamWriter(System.out);
		
		PrettyXMLStreamWriter xml = new PrettyXMLStreamWriter(writer);
		
		xml.writeStartDocument();
		xml.writeStartElement("feed");
		xml.writeStartElement("entry");
		xml.writeStartElement("dog");
		xml.writeAttribute("sound", "woof");
		xml.writeCharacters("Aloha!");
		xml.writeEndElement();
		xml.writeEndElement();
		xml.writeEndElement();
		xml.writeEndDocument();
		xml.flush();
	}
	
	public PrettyXMLStreamWriter(XMLStreamWriter target) {
		this.target = target;
	}

	public void close() throws XMLStreamException {
		target.close();
	}

	public void flush() throws XMLStreamException {
		target.flush();
	}

	public NamespaceContext getNamespaceContext() {
		return target.getNamespaceContext();
	}

	public String getPrefix(String uri) throws XMLStreamException {
		return target.getPrefix(uri);
	}

	public Object getProperty(String name) throws IllegalArgumentException {
		return target.getProperty(name);
	}

	public void setDefaultNamespace(String uri) throws XMLStreamException {
		target.setDefaultNamespace(uri);
	}

	public void setNamespaceContext(NamespaceContext context) throws XMLStreamException {
		target.setNamespaceContext(context);
	}

	public void setPrefix(String prefix, String uri) throws XMLStreamException {
		target.setPrefix(prefix, uri);
	}

	public void writeAttribute(String prefix, String namespaceURI, String localName, String value) throws XMLStreamException {
		target.writeAttribute(prefix, namespaceURI, localName, value);
		lastEventType = ATTRIBUTE;
	}

	public void writeAttribute(String namespaceURI, String localName, String value) throws XMLStreamException {
		target.writeAttribute(namespaceURI, localName, value);
		lastEventType = ATTRIBUTE;
	}

	public void writeAttribute(String localName, String value) throws XMLStreamException {
		target.writeAttribute(localName, value);
		lastEventType = ATTRIBUTE;
	}

	public void writeCData(String data) throws XMLStreamException {
		target.writeCData(data);
		lastEventType = CDATA;
	}

	public void writeCharacters(char[] text, int start, int len) throws XMLStreamException {
		target.writeCharacters(text, start, len);
		lastEventType = CHARACTERS;
	}

	public void writeCharacters(String text) throws XMLStreamException {
		target.writeCharacters(text);
		lastEventType = CHARACTERS;
	}

	public void writeComment(String data) throws XMLStreamException {
		target.writeComment(data);
		lastEventType = COMMENT;
	}

	public void writeDefaultNamespace(String namespaceURI) throws XMLStreamException {
		target.writeDefaultNamespace(namespaceURI);
		lastEventType = NAMESPACE;
	}

	public void writeDTD(String dtd) throws XMLStreamException {
		target.writeDTD(dtd);
		lastEventType = DTD;
	}

	public void writeEmptyElement(String prefix, String localName, String namespaceURI) throws XMLStreamException {
		beforeWriteStartElement();
		target.writeEmptyElement(prefix, localName, namespaceURI);
		lastEventType = END_ELEMENT;
	}

	public void writeEmptyElement(String namespaceURI, String localName) throws XMLStreamException {
		beforeWriteStartElement();
		target.writeEmptyElement(namespaceURI, localName);
		lastEventType = END_ELEMENT;
	}

	public void writeEmptyElement(String localName) throws XMLStreamException {
		beforeWriteStartElement();
		target.writeEmptyElement(localName);
		lastEventType = END_ELEMENT;
	}

	public void writeEndDocument() throws XMLStreamException {
		target.writeEndDocument();
		lastEventType = END_DOCUMENT;
	}

	public void writeEndElement() throws XMLStreamException {
		depth--;
		switch (lastEventType) {
		case END_ELEMENT:
			target.writeCharacters(endOfLine);
			for (int i = 0; i < depth; i++) {
				target.writeCharacters(tab);
			}
			break;
		}
		target.writeEndElement();
		lastEventType = END_ELEMENT;
	}

	public void writeEntityRef(String name) throws XMLStreamException {
		target.writeEntityRef(name);
		lastEventType = ENTITY_REFERENCE;
	}

	public void writeNamespace(String prefix, String namespaceURI) throws XMLStreamException {
		target.writeNamespace(prefix, namespaceURI);
		lastEventType = NAMESPACE;
	}

	public void writeProcessingInstruction(String target, String data) throws XMLStreamException {
		this.target.writeProcessingInstruction(target, data);
		lastEventType = PROCESSING_INSTRUCTION;
	}

	public void writeProcessingInstruction(String target) throws XMLStreamException {
		this.target.writeProcessingInstruction(target);
		lastEventType = PROCESSING_INSTRUCTION;
	}

	public void writeStartDocument() throws XMLStreamException {
		target.writeStartDocument();
		lastEventType = START_DOCUMENT;
	}

	public void writeStartDocument(String encoding, String version) throws XMLStreamException {
		target.writeStartDocument(encoding, version);
		lastEventType = START_DOCUMENT;
	}

	public void writeStartDocument(String version) throws XMLStreamException {
		target.writeStartDocument(version);
		lastEventType = START_DOCUMENT;
	}

	protected void beforeWriteStartElement() throws XMLStreamException {
		switch (lastEventType) {
		case START_ELEMENT:
		case NAMESPACE:
		case ATTRIBUTE:
		case END_ELEMENT:
			target.writeCharacters(endOfLine);
			for (int i = 0; i < depth; i++) {
				target.writeCharacters(tab);
			}
			break;
		case START_DOCUMENT:
			target.writeCharacters(endOfLine);
			break;
		}
	}

	protected void afterWriteStartElement() throws XMLStreamException {
		lastEventType = START_ELEMENT;
		depth++;
	}

	public void writeStartElement(String prefix, String localName, String namespaceURI) throws XMLStreamException {
		beforeWriteStartElement();
		target.writeStartElement(prefix, localName, namespaceURI);
		afterWriteStartElement();
	}

	public void writeStartElement(String namespaceURI, String localName) throws XMLStreamException {
		beforeWriteStartElement();
		target.writeStartElement(namespaceURI, localName);
		afterWriteStartElement();
	}

	public void writeStartElement(String localName) throws XMLStreamException {
		beforeWriteStartElement();
		target.writeStartElement(localName);
		afterWriteStartElement();
	}

}
