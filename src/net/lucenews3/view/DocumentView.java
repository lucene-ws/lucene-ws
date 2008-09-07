package net.lucenews3.view;

import java.util.Date;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import net.lucenews3.DocumentMetaData;

public abstract class DocumentView extends XMLStreamView {

	public String format(Date date) {
		return null; // TODO
	}

	public void writeEntry(XMLStreamWriter xml, DocumentMetaData metaData, boolean includeDefaultNamespace) throws XMLStreamException {
		xml.writeStartElement("entry");
		
		if (includeDefaultNamespace) {
			//xml.write
		}
	}

}
