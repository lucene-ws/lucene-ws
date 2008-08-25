package net.lucenews3;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLStreamWriter;

public class APPIntrospectionView extends XMLStreamView {

	private Service service;

	public APPIntrospectionView() {
		setContentType("application/atomserv+xml");
	}

	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void renderMergedOutputModel(Map model, HttpServletRequest request, HttpServletResponse response, XMLStreamWriter xml) throws Exception {
		List<Index> indexes = (List<Index>) model.get("indexes");
		
		xml.writeStartDocument();
		xml.writeStartElement("service");
		xml.writeDefaultNamespace("http://www.w3.org/2007/app");
		xml.writeNamespace("atom", "http://www.w3.org/2005/Atom");
		
		xml.writeStartElement("workspace");
		
		for (Index index : indexes) {
			xml.writeStartElement("collection");
			xml.writeAttribute("href", service.getIndexURI(request, index));
			String displayName = index.getDisplayName();
			if (displayName == null) {
				// Ignore
			} else {
				xml.writeAttribute("http://www.w3.org/2005/Atom", "title", displayName);
			}
			
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
