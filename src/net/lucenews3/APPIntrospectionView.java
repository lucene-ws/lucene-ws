package net.lucenews3;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLStreamWriter;

public class APPIntrospectionView extends XMLStreamView {

	private String appNamespaceURI;

	@SuppressWarnings("unchecked")
	@Override
	protected void renderMergedOutputModel(Map model, HttpServletRequest req, HttpServletResponse res, XMLStreamWriter xml) throws Exception {
		// TODO Auto-generated method stub
		List<Index> indexes = (List<Index>) model.get("indexes");
		
		xml.writeStartDocument();
		
		xml.writeStartElement("service");
		xml.writeAttribute("xmlns", appNamespaceURI);
		
		xml.writeStartElement("workspace");
		
		for (Index index : indexes) {
			xml.writeStartElement("collection");
			xml.writeStartElement("href", null); // TODO
			xml.writeAttribute("title", index.getDisplayName());
			
			xml.writeStartElement("member-type");
			xml.writeCharacters("entry");
			xml.writeEndElement(); // member-type
			
			xml.writeEndElement(); // collection
		}
		
		xml.writeEndElement(); // workspace
		
		xml.writeEndElement(); // service
		
		xml.writeEndDocument();
	}

}
