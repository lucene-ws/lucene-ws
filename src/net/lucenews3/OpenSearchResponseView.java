package net.lucenews3;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import net.lucenews3.view.XMLStreamView;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.search.Hits;

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
		Hits hits = (Hits) model.get("hits");
		int totalResults = hits.length();
		
		xml.writeStartDocument();
		
		xml.writeStartElement("feed");
		xml.writeDefaultNamespace("http://www.w3.org/2005/Atom");
		xml.writeNamespace("opensearch", "http://a9.com/-/spec/opensearch/1.1/");
		xml.writeNamespace("relevance", "http://a9.com/-/opensearch/extensions/relevance/1.0/");
		
		xml.writeStartElement("opensearch", "totalResults", "http://a9.com/-/spec/opensearch/1.1/");
		xml.writeCharacters(String.valueOf(totalResults));
		xml.writeEndElement();
		
		xml.writeStartElement("opensearch", "startIndex", "http://a9.com/-/spec/opensearch/1.1/");
		xml.writeCharacters(String.valueOf(1));
		xml.writeEndElement();
		
		xml.writeStartElement("opensearch", "itemsPerPage", "http://a9.com/-/spec/opensearch/1.1/");
		xml.writeCharacters(String.valueOf(10));
		xml.writeEndElement();
		
		xml.writeEmptyElement("opensearch", "Query", "http://a9.com/-/spec/opensearch/1.1/");
		xml.writeAttribute("role", "request");
		xml.writeAttribute("searchTerms", "kfjas;fdsadf");
		
		xml.writeEmptyElement("link");
		xml.writeAttribute("rel", "first");
		xml.writeAttribute("href", "http://...");
		xml.writeAttribute("type", "application/atom+xml");
		
		for (int n = 0; n < hits.length(); n++) {
			Document document = hits.doc(n);
			
			xml.writeStartElement("entry");
			
			xml.writeStartElement("title");
			xml.writeCharacters("TODO: title"); // TODO
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
			xml.writeStartElement("relevance", "score", "http://a9.com/-/opensearch/extensions/relevance/1.0/");
			xml.writeCharacters(String.valueOf(hits.score(n)));
			xml.writeEndElement();
			
			xml.writeStartElement("content");
			xml.writeAttribute("type", "xhtml");
			
			xml.writeStartElement("div");
			xml.writeAttribute("xmlns", "http://www.w3.org/1999/xhtml");
			
			xml.writeStartElement("dl");
			for (Field field : (List<Field>) document.getFields()) {
				xml.writeStartElement("dt");
				
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
				
				xml.writeAttribute("class", className);
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
