package net.lucenews3;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLStreamWriter;

public class ExceptionView extends XMLStreamView {

	@SuppressWarnings("unchecked")
	@Override
	protected void renderMergedOutputModel(Map model, HttpServletRequest req, HttpServletResponse res, XMLStreamWriter xml) throws Exception {
		Exception ex = (Exception) model.get("exception");
		
		xml.writeStartDocument();
		xml.writeStartElement("entry");
		
		xml.writeStartElement("title");
		xml.writeCharacters(ex.getClass().getCanonicalName());
		xml.writeEndElement();
		
		xml.writeStartElement("updated");
		xml.writeCharacters(""); // TODO
		xml.writeEndElement();
		
		xml.writeStartElement("author");
		xml.writeStartElement("name");
		xml.writeCharacters("Lucene Web Service");
		xml.writeEndElement();
		xml.writeEndElement();
		
		xml.writeStartElement("id");
		xml.writeCharacters(""); // TODO
		xml.writeEndElement();

		String message = ex.getMessage();
		if (message == null) {
			xml.writeEmptyElement("summary");
		} else {
			xml.writeStartElement("summary");
			xml.writeCharacters(ex.getMessage());
			xml.writeEndElement();
		}
		
		xml.writeStartElement("content");
		xml.writeAttribute("type", "xhtml");
		xml.writeStartElement("div");
		
		xml.writeStartElement("ol");
		
		StackTraceElement[] elements = ex.getStackTrace();
		for (StackTraceElement element : elements) {
			xml.writeStartElement("li");
			xml.writeCharacters(element.toString());
			xml.writeEndElement();
		}
		
		xml.writeEndElement(); // </ol>
		
		xml.writeEndElement(); // </div>
		xml.writeEndElement(); // </content>
		
		xml.writeEndElement(); // </entry>
		xml.writeEndDocument();
	}

}
