package net.lucenews3;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

public class OpenSearchResponseView extends XMLStreamView {

	@SuppressWarnings("unchecked")
	public static void main(String... args) throws Exception {
		OpenSearchResponseView view = new OpenSearchResponseView();
		Map model = new HashMap();
		model.put("results", new DefaultResults(null));
		view.renderMergedOutputModel(model, null, null, XMLOutputFactory.newInstance().createXMLStreamWriter(System.out));
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void renderMergedOutputModel(Map model, HttpServletRequest req, HttpServletResponse res, XMLStreamWriter xml) throws Exception {
		// TODO Auto-generated method stub
		Results results = (Results) model.get("results");
		
		xml.writeStartDocument();
		
		xml.writeStartElement("feed");
		xml.writeAttribute("xmlns", "http://www.w3.org/2005/Atom");
		// TODO xml.writeAttribute("", "xmlns:opensearch", "http://a9.com/-/spec/opensearch/1.1/");
		
		for (Result result : results) {
			Document document = result.getDocument();
			
			xml.writeStartElement("entry");
			
			xml.writeStartElement("title");
			xml.writeCharacters(result.getTitle());
			xml.writeEndElement();
			
			xml.writeStartElement("link");
			xml.writeCharacters(""); // TODO
			xml.writeEndElement();
			
			xml.writeStartElement("updated");
			xml.writeCharacters(""); // TODO
			xml.writeEndElement();
			
			xml.writeStartElement("id");
			xml.writeCharacters(""); // TODO
			xml.writeEndElement();
			
			xml.writeStartElement("summary");
			xml.writeCharacters(""); // TODO
			xml.writeEndElement();
			
			// TODO xml.writeStartElement("http://a9.com/-/spec/opensearch/1.1/", "opensearch:relevance");
			xml.writeStartElement("relevance");
			xml.writeCharacters(String.valueOf(result.getRelevance()));
			xml.writeEndElement();
			
			xml.writeStartElement("content");
			xml.writeAttribute("type", "xhtml");
			
			xml.writeStartElement("div");
			xml.writeAttribute("xmlns", "http://www.w3.org/1999/xhtml");
			
			xml.writeStartElement("dl");
			for (Field field : (List<Field>) document.getFields()) {
				xml.writeStartElement("dt");
				xml.writeAttribute("class", ""); // TODO
				xml.writeCharacters(field.name());
				xml.writeEndElement();
				
				xml.writeStartElement("dd");
				xml.writeCharacters(String.valueOf(field.stringValue()));
				xml.writeEndElement();
			}
			xml.writeEndElement(); // dl
			xml.writeEndElement(); // content
			xml.writeEndElement(); // entry
		}
		
		xml.writeEndElement(); // feed
		xml.writeEndDocument();
		xml.flush();
	}

}
