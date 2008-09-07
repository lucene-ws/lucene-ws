package net.lucenews3;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLStreamWriter;

import net.lucenews3.view.XMLStreamView;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.springframework.util.Assert;

public class DocumentXMLView extends XMLStreamView {

	@SuppressWarnings("unchecked")
	@Override
	protected void renderMergedOutputModel(Map model, HttpServletRequest request, HttpServletResponse response, XMLStreamWriter xml) throws Exception {
		Assert.isTrue(model.containsKey("document"), "\"document\" key in model must be set to a Lucene document");
		
		Document document = (Document) model.get("document");
		Assert.notNull(document, "Lucene document must not be null");
		
		xml.writeStartDocument();
		
		xml.writeStartElement("entry");
		xml.writeDefaultNamespace("http://www.w3.org/2005/Atom");
		
		xml.writeStartElement("content");
		xml.writeAttribute("type", "xhtml");
		xml.writeStartElement("div");
		xml.writeDefaultNamespace("http://www.w3.org/1999/xhtml");
		xml.writeStartElement("dl");
		xml.writeAttribute("class", "xoxo");
		
		List<Field> fields = document.getFields();
		if (fields == null) {
			// Huh?
		} else {
			for (Field field : fields) {
				String className;
				if (field.isStored()) {
					if (field.isTokenized()) {
						className = "stored indexed tokenized";
					} else if (field.isIndexed()) {
						className = "stored indexed";
					} else {
						className = "stored";
					}
				} else {
					if (field.isTokenized()) {
						className = "indexed tokenized";
					} else if (field.isIndexed()) {
						className = "indexed";
					} else {
						className = "";
					}
				}
				
				xml.writeStartElement("dt");
				xml.writeAttribute("class", className);
				xml.writeCharacters(field.name());
				xml.writeEndElement(); // dt
				
				xml.writeStartElement("dd");
				xml.writeCharacters(field.stringValue());
				xml.writeEndElement(); // dd
			}
		}
		
		xml.writeEndElement(); // dl
		xml.writeEndElement(); // div
		xml.writeEndElement(); // content
		xml.writeEndElement(); // entry
		
		xml.writeEndDocument();
	}

}
