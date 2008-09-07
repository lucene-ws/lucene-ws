package net.lucenews3;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLStreamWriter;

import net.lucenews3.view.XMLStreamView;

public class OpenSearchDescriptionView extends XMLStreamView {

	public OpenSearchDescriptionView() {
		setContentType("application/opensearchdescription+xml;charset=utf-8");
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void renderMergedOutputModel(Map model, HttpServletRequest request, HttpServletResponse response, XMLStreamWriter xml) throws Exception {
		// TODO Auto-generated method stub
		xml.writeStartDocument();
		
		xml.writeStartElement("OpenSearchDescription");
		
		xml.writeStartElement("ShortName");
		xml.writeCharacters("");
		xml.writeEndElement();
		
		xml.writeStartElement("Description");
		xml.writeCharacters("");
		xml.writeEndElement();
		
		xml.writeStartElement("Url");
		xml.writeAttribute("template", "http://localhost:8080/lucene/test?q={searchTerms}&start-index={startIndex}&max-results={count}");
		xml.writeAttribute("type", "application/atom+xml");
		xml.writeEndElement();
		
		xml.writeEndElement(); // </OpenSearchDescription>
		
		xml.writeEndDocument();
	}

}
