package net.lucenews3.controller;

import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.lucenews3.lucene.support.Document;
import net.lucenews3.lucene.support.DocumentList;
import net.lucenews3.lucene.support.FieldList;
import net.lucenews3.lucene.support.Index;
import net.lucenews3.lucene.support.IndexProvider;
import net.lucenews3.lucene.support.Result;
import net.lucenews3.lucene.support.ResultList;

import org.apache.lucene.document.Field;
import org.apache.lucene.search.Query;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.Controller;

public class SearchIndexController implements Controller {

	private IndexProvider indexProvider;
	
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		final Index index = indexProvider.getIndex(request.getAttribute("index").toString());
		final Query query = null;
		final DocumentList documents = index.getDocuments();
		final ResultList results = documents.searchBy(query);
		
		// Temporary ModelAndView for experimental purposes
		return new ModelAndView(new View() {

			public String getContentType() {
				return "text/html";
			}

			public void render(Map arg0, HttpServletRequest request,
					HttpServletResponse response) throws Exception {
				PrintWriter out = response.getWriter();
				out.println("<p>Well, howdy. Looks like you're aiming to do some searchin! Yee-haw!</p>");

				out.println("<p>You're in luck! The index contains "
								+ documents.size()
								+ " documents! Surely one of them matches your search...</p>");

				out.println("<p>Your search for \"" + query + "\" returned "
						+ results.size() + " results!</p>");

				out.println("<ol>");
				for (Result result : results) {
					Document document = result.getDocument();
					FieldList fields = document.getFields();
					Field stave = fields.byName("stave").first();
					Field paragraph = fields.byName("paragraph").first();
					Field text = fields.byName("text").first();
					
					out.println("<li>Stave " + stave.stringValue() + ", paragraph " + paragraph.stringValue() + ": <blockquote>" + text.stringValue() + "</blockquote></li>");
				}
				out.println("</ol>");
			}});
	}

}
