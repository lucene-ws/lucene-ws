package net.lucenews3;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class AtomPropertiesParser extends AtomParser {

	protected List<Properties> propertiesList;
	protected Properties currentProperties;

	public AtomPropertiesParser(XMLStreamReader xml) {
		super(xml);
	}

	@Override
	public void parseFeed() throws XMLStreamException {
		propertiesList = new ArrayList<Properties>();
		super.parseFeed();
	}

	@Override
	public void parseEntry() throws XMLStreamException {
		currentProperties = new Properties();
		super.parseEntry();
		
		if (propertiesList == null) {
			propertiesList = new SingleElementList<Properties>(currentProperties);
		} else {
			propertiesList.add(currentProperties);
		}
	}

	@Override
	protected void parseXOXOTermDefinition() throws XMLStreamException {
		currentProperties.put(xoxoTerm, xoxoDefinition);
	}

	public List<Properties> getPropertiesList() {
		return propertiesList;
	}

	public Properties getProperties() {
		return propertiesList.get(0);
	}

}
