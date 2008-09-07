package net.lucenews3.view;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLStreamWriter;

import org.apache.lucene.document.Document;

import net.lucenews3.DocumentMetaData;
import net.lucenews3.Index;
import net.lucenews3.Service;

public class DocumentInsertView extends DocumentView {

	@SuppressWarnings("unchecked")
	@Override
	protected void renderMergedOutputModel(Map model, HttpServletRequest request, HttpServletResponse response, XMLStreamWriter xml) throws Exception {
		// TODO Auto-generated method stub
		DocumentMetaData metaData = (DocumentMetaData) model.get("document");
		Index index = metaData.getIndex();
		Document document = metaData.getDocument();
		Service service = getService();
		
		xml.writeStartDocument();
		xml.writeStartElement("entry");
		xml.writeDefaultNamespace("http://www.w3.org/2005/Atom");
		
		xml.writeStartElement("title");
		xml.writeCharacters("OK");
		xml.writeEndElement();
		
		xml.writeStartElement("updated");
		xml.writeCharacters(format(metaData.getLastUpdated()));
		xml.writeEndElement();
		
		xml.writeStartElement("id");
		xml.writeCharacters(service.getDocumentURI(request, index, document).toString());
		xml.writeEndElement();
		
		xml.writeStartElement("summary");
		xml.writeCharacters("OK");
		xml.writeEndElement();
		
		xml.writeEndElement(); // entry
		xml.writeEndDocument();
	}

}
