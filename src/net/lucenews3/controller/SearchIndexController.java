package net.lucenews3.controller;

import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.lucenews3.lucene.support.Document;
import net.lucenews3.lucene.support.DocumentList;
import net.lucenews3.lucene.support.FieldList;
import net.lucenews3.lucene.support.Index;
import net.lucenews3.lucene.support.Result;
import net.lucenews3.lucene.support.ResultList;
import net.lucenews3.lucene.support.SearchRequest;
import net.lucenews3.lucene.support.SearchRequestParser;

import org.apache.lucene.document.Field;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

public class SearchIndexController extends AbstractController {
	
	private SearchRequestParser<HttpServletRequest> searchRequestParser;
	
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		final Index index = service.getIndex(request.getAttribute("indexName").toString());
		final SearchRequest searchRequest = searchRequestParser.parseSearchRequest(request);
		final DocumentList documents = index.getDocuments();
		final ResultList results = documents.searchBy(searchRequest);
		
		// Temporary ModelAndView for experimental purposes
		return new ModelAndView(new View() {

			public String getContentType() {
				return "text/html";
			}

			@SuppressWarnings("unchecked")
			public void render(Map arg0, HttpServletRequest request,
					HttpServletResponse response) throws Exception {
				PrintWriter out = response.getWriter();
				out.println("<p>Well, howdy. Looks like you're aiming to do some searchin! Yee-haw!</p>");

				out.println("<p>You're in luck! The index contains "
								+ documents.size()
								+ " documents! Surely one of them matches your search...</p>");

				out.println("<p>Your search for \"" + searchRequest.getQuery() + "\" returned "
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
