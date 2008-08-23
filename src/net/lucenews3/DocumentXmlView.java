package net.lucenews3;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLStreamWriter;

import org.apache.lucene.document.Document;

public class DocumentXmlView extends XMLStreamView {

	@SuppressWarnings("unchecked")
	@Override
	protected void renderMergedOutputModel(Map model, HttpServletRequest req, HttpServletResponse res, XMLStreamWriter xml) throws Exception {
		// TODO Auto-generated method stub
		List<Document> documents = (List<Document>) model.get("documents");
		
		xml.writeStartDocument();
		
		if (documents instanceof SingleElementList) {
			// Just display the <entry>
			Document document = documents.get(0);
			writeDocument(document, xml);
		} else {
			// Display a <feed>
			xml.writeStartElement("feed");
			for (Document document : documents) {
				writeDocument(document, xml);
			}
			xml.writeEndElement();
		}
		
		xml.writeEndDocument();
	}
	
	protected void writeDocument(Document document, XMLStreamWriter xml) throws Exception {
		xml.writeStartElement("entry");
		xml.writeEndElement();
	}

}
