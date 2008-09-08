package net.lucenews3.view;

import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLStreamWriter;


import org.springframework.util.Assert;

public class PropertiesXMLView extends XMLStreamView {

	public PropertiesXMLView() {
		setContentType("application/atom+xml");
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void renderMergedOutputModel(Map model, HttpServletRequest request, HttpServletResponse response, XMLStreamWriter xml) throws Exception {
		Assert.isTrue(model.containsKey("properties"), "\"properties\" key in model must be set to an instance of java.util.Properties");
		
		Properties properties = (Properties) model.get("properties");
		
		xml.writeStartDocument();
		xml.writeStartElement("entry");
		xml.writeDefaultNamespace("http://www.w3.org/2005/Atom");
		xml.writeStartElement("content");
		xml.writeAttribute("type", "xhtml");
		xml.writeStartElement("div");
		xml.writeDefaultNamespace("http://www.w3.org/1999/xhtml");
		xml.writeStartElement("dt");
		
		for (Entry<Object, Object> entry : properties.entrySet()) {
			Object key = entry.getKey();
			if (key == null) {
				xml.writeEmptyElement("dt");
			} else {
				xml.writeStartElement("dt");
				xml.writeCharacters(String.valueOf(key));
				xml.writeEndElement(); // dt
			}
			
			Object value = entry.getValue();
			if (value == null) {
				xml.writeEmptyElement("dd");
			} else {
				xml.writeStartElement("dd");
				xml.writeCharacters(String.valueOf(value));
				xml.writeEndElement(); // dd
			}
		}
		
		xml.writeEndElement(); // dt
		xml.writeEndElement(); // div
		xml.writeEndElement(); // content
		xml.writeEndElement(); // entry
		xml.writeEndDocument();
	}

}
