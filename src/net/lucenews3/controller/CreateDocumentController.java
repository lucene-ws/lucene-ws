package net.lucenews3.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.lucenews3.atom.Entry;
import net.lucenews3.lucene.support.Document;
import net.lucenews3.lucene.support.DocumentList;
import net.lucenews3.lucene.support.Index;
import net.lucenews3.lucene.support.NativeDocumentDocument;

import org.apache.lucene.document.Field;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class CreateDocumentController implements Controller {

	private IndexAccess indexAccess;
	private EntryResolver entryResolver;
	
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Index index = indexAccess.get(request.getAttribute("index").toString());
		DocumentList documents = index.getDocuments();
		
		List<Entry> entries = entryResolver.resolveEntries(request);
		
		for (Entry entry : entries) {
			Document document = buildDocument(entry);
			documents.add(document);
		}
		
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Builds a document from an Atom entry.
	 * @param entry
	 * @return
	 */
	public Document buildDocument(Entry entry) {
		Document result = new NativeDocumentDocument();
		buildFields(entry, result.getFields());
		return result;
	}
	
	public List<Field> buildFields(Entry entry) {
		List<Field> results = new ArrayList<Field>();
		return results;
	}
	
	public void buildFields(Entry entry, List<Field> fields) {
		
	}

}
