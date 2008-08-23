package net.lucenews3;

import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class PrettyXMLStreamWriter implements XMLStreamWriter {

	private final XMLStreamWriter target;

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
	}

	public void writeAttribute(String namespaceURI, String localName, String value) throws XMLStreamException {
		target.writeAttribute(namespaceURI, localName, value);
	}

	public void writeAttribute(String localName, String value) throws XMLStreamException {
		target.writeAttribute(localName, value);
	}

	public void writeCData(String data) throws XMLStreamException {
		target.writeCData(data);
	}

	public void writeCharacters(char[] text, int start, int len) throws XMLStreamException {
		target.writeCharacters(text, start, len);
	}

	public void writeCharacters(String text) throws XMLStreamException {
		target.writeCharacters(text);
	}

	public void writeComment(String data) throws XMLStreamException {
		target.writeComment(data);
	}

	public void writeDefaultNamespace(String namespaceURI) throws XMLStreamException {
		target.writeDefaultNamespace(namespaceURI);
	}

	public void writeDTD(String dtd) throws XMLStreamException {
		target.writeDTD(dtd);
	}

	public void writeEmptyElement(String prefix, String localName, String namespaceURI) throws XMLStreamException {
		target.writeEmptyElement(prefix, localName, namespaceURI);
	}

	public void writeEmptyElement(String namespaceURI, String localName) throws XMLStreamException {
		target.writeEmptyElement(namespaceURI, localName);
	}

	public void writeEmptyElement(String localName) throws XMLStreamException {
		target.writeEmptyElement(localName);
	}

	public void writeEndDocument() throws XMLStreamException {
		target.writeEndDocument();
	}

	public void writeEndElement() throws XMLStreamException {
		target.writeEndElement();
	}

	public void writeEntityRef(String name) throws XMLStreamException {
		target.writeEntityRef(name);
	}

	public void writeNamespace(String prefix, String namespaceURI) throws XMLStreamException {
		target.writeNamespace(prefix, namespaceURI);
	}

	public void writeProcessingInstruction(String target, String data) throws XMLStreamException {
		this.target.writeProcessingInstruction(target, data);
	}

	public void writeProcessingInstruction(String target) throws XMLStreamException {
		this.target.writeProcessingInstruction(target);
	}

	public void writeStartDocument() throws XMLStreamException {
		target.writeStartDocument();
	}

	public void writeStartDocument(String encoding, String version) throws XMLStreamException {
		target.writeStartDocument(encoding, version);
	}

	public void writeStartDocument(String version) throws XMLStreamException {
		target.writeStartDocument(version);
	}

	public void writeStartElement(String prefix, String localName, String namespaceURI) throws XMLStreamException {
		target.writeStartElement(prefix, localName, namespaceURI);
	}

	public void writeStartElement(String namespaceURI, String localName) throws XMLStreamException {
		target.writeStartElement(namespaceURI, localName);
	}

	public void writeStartElement(String localName) throws XMLStreamException {
		target.writeStartElement(localName);
	}

}
