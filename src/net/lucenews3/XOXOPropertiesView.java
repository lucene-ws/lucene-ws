package net.lucenews3;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLStreamWriter;

import net.lucenews3.view.XMLStreamView;

public class XOXOPropertiesView extends XMLStreamView {

	@SuppressWarnings("unchecked")
	@Override
	protected void renderMergedOutputModel(Map model, HttpServletRequest req, HttpServletResponse res, XMLStreamWriter xml) throws Exception {
		// TODO Auto-generated method stub
		
		Iterable<Map.Entry<String, String>> properties = (Iterable<Map.Entry<String, String>>) model.get("properties");
		
		xml.writeStartElement("entry");
		
		xml.writeStartElement("title");
		xml.writeCharacters(""); // TODO
		xml.writeEndElement();
		
		xml.writeStartElement("updated");
		xml.writeCharacters(""); // TODO
		xml.writeEndElement();
		
		xml.writeStartElement("author");
		xml.writeCharacters(""); // TODO
		xml.writeEndElement();
		
		xml.writeStartElement("id");
		xml.writeCharacters(""); // TODO
		xml.writeEndElement();
		
		xml.writeStartElement("content");
		xml.writeAttribute("type", "xhtml");
		
		xml.writeStartElement("div");
		// TODO write XHTML namespace xml.w
		
		xml.writeStartElement("dt");
		xml.writeAttribute("class", "xoxo");
		
		for (Map.Entry<String, String> property : properties) {
			String key = property.getKey();
			String value = property.getValue();
			
			if (key == null) {
				xml.writeEmptyElement("dt");
			} else {
				xml.writeStartElement("dt");
				xml.writeCharacters(key);
				xml.writeEndElement();
			}
			
			if (value == null) {
				xml.writeEmptyElement("dd");
			} else {
				xml.writeStartElement("dd");
				xml.writeCharacters(value);
				xml.writeEndElement();
			}
			
			xml.writeEndElement();
		}
		
		xml.writeEndElement(); // </dt>
		
		xml.writeEndElement(); // </content>
		
		xml.writeEndElement();
	}

}
