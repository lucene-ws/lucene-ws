package net.lucenews3;

import java.util.Properties;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class AtomPropertiesParser extends AtomParser {

	protected Properties properties;

	public AtomPropertiesParser(XMLStreamReader xml) {
		super(xml);
	}

	@Override
	public void parseEntry() throws XMLStreamException {
		properties = new Properties();
		super.parseEntry();
		// TODO export properties
	}

	@Override
	protected void parseXOXOTermDefinition() throws XMLStreamException {
		properties.put(xoxoTerm, xoxoDefinition);
	}

}
