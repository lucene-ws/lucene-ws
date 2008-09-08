package net.lucenews3.view;

import java.net.URI;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLStreamWriter;

import net.lucenews3.DocumentMetaData;
import net.lucenews3.Index;
import net.lucenews3.Result;
import net.lucenews3.Results;
import net.lucenews3.Service;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

public class OpenSearchResponseView extends XMLStreamView {

	private String atomNamespaceURI = "http://www.w3.org/2005/Atom";
	private String openSearchNamespaceURI = "http://a9.com/-/spec/opensearch/1.1/";
	private String openSearchRelevanceNamespaceURI = "http://a9.com/-/opensearch/extensions/relevance/1.0/";
	private String xhtmlNamespaceURI = "http://www.w3.org/1999/xhtml";
	private DateFormat atomDateFormat;

	@SuppressWarnings("unchecked")
	@Override
	protected void renderMergedOutputModel(Map model, HttpServletRequest request, HttpServletResponse response, XMLStreamWriter xml) throws Exception {
		// TODO Auto-generated method stub
		Service service = getService();
		Results results = (Results) model.get("results");
		Integer totalResults = results.size();
		
		xml.writeStartDocument();
		
		xml.writeStartElement("feed");
		xml.writeDefaultNamespace(atomNamespaceURI);
		xml.writeNamespace("opensearch", openSearchNamespaceURI);
		xml.writeNamespace("relevance", openSearchRelevanceNamespaceURI);
		
		Date updated = service.getIndex(request).getLastUpdated();
		if (updated != null) {
			xml.writeStartElement("updated");
			xml.writeCharacters(atomDateFormat.format(updated));
			xml.writeEndElement();
		}
		
		writeAuthor(model, request, response, xml);
		
		xml.writeStartElement("opensearch", "totalResults", openSearchNamespaceURI);
		xml.writeCharacters(String.valueOf(totalResults));
		xml.writeEndElement();
		
		xml.writeStartElement("opensearch", "startIndex", openSearchNamespaceURI);
		xml.writeCharacters(String.valueOf(1));
		xml.writeEndElement();
		
		xml.writeStartElement("opensearch", "itemsPerPage", openSearchNamespaceURI);
		xml.writeCharacters(String.valueOf(10));
		xml.writeEndElement();
		
		String searchTerms = results.getSearchTerms();
		if (searchTerms != null) {
			xml.writeEmptyElement("opensearch", "Query", openSearchNamespaceURI);
			xml.writeAttribute("role", "request");
			xml.writeAttribute("searchTerms", searchTerms);
		}
		
		xml.writeEmptyElement("link");
		xml.writeAttribute("rel", "first");
		xml.writeAttribute("href", "http://...");
		xml.writeAttribute("type", "application/atom+xml");
		
		for (Result result : results) {
			writeResult(model, request, response, xml, result);
		}
		
		xml.writeEndElement(); // feed
		xml.writeEndDocument();
	}

	@SuppressWarnings("unchecked")
	protected void writeAuthor(Map model, HttpServletRequest request, HttpServletResponse response, XMLStreamWriter xml) throws Exception {
		xml.writeStartElement("author");
		xml.writeEndElement();
	}

	@SuppressWarnings("unchecked")
	protected void writeResult(Map model, HttpServletRequest request, HttpServletResponse response, XMLStreamWriter xml, Result result) throws Exception {
		Document document = result.getDocument();
		DocumentMetaData documentMetaData = result.getDocumentMetaData();
		Index index = documentMetaData.getIndex();
		Date lastUpdated = documentMetaData.getLastUpdated();
		
		URI documentURI = getService().getDocumentURI(request, index, document);
		
		xml.writeStartElement("entry");
		
		xml.writeStartElement("title");
		xml.writeCharacters("TODO: title"); // TODO
		xml.writeEndElement();
		
		xml.writeStartElement("link");
		xml.writeCharacters(documentURI.toString());
		xml.writeEndElement();
		
		xml.writeStartElement("updated");
		xml.writeCharacters(String.valueOf(lastUpdated)); // TODO
		xml.writeEndElement();
		
		xml.writeStartElement("id");
		xml.writeCharacters(documentURI.toString()); // TODO
		xml.writeEndElement();
		
		xml.writeStartElement("summary");
		xml.writeCharacters(""); // TODO
		xml.writeEndElement();
		
		// TODO xml.writeStartElement("http://a9.com/-/spec/opensearch/1.1/", "opensearch:relevance");
		xml.writeStartElement("relevance", "score", openSearchRelevanceNamespaceURI);
		xml.writeCharacters(String.valueOf(result.getScore()));
		xml.writeEndElement();
		
		xml.writeStartElement("content");
		xml.writeAttribute("type", "xhtml");
		
		xml.writeStartElement("div");
		xml.writeAttribute("xmlns", xhtmlNamespaceURI);
		
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

}
