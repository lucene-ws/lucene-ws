package net.lucenews.controller;

import java.io.IOException;
import java.util.Calendar;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import net.lucenews.LuceneContext;
import net.lucenews.LuceneRequest;
import net.lucenews.LuceneResponse;
import net.lucenews.LuceneWebService;
import net.lucenews.ServletUtils;
import net.lucenews.atom.Author;
import net.lucenews.atom.Content;
import net.lucenews.atom.Entry;
import net.lucenews.atom.Text;
import net.lucenews.model.LuceneIndexManager;
import net.lucenews.view.AtomView;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ExceptionController extends Controller {

	public static void process(LuceneContext c, Exception e) {
		try {
			process(c, e, ServletUtils.parseBoolean(c.getService().getProperty(
					"service.debugging")));
		} catch (IOException ioe) {
		}
	}

	public static void process(LuceneContext c, Exception e,
			Boolean doStackTrace) {
		LuceneWebService service = c.getService();
		LuceneIndexManager manager = service.getIndexManager();
		LuceneRequest request = c.getRequest();
		LuceneResponse response = c.getResponse();

		Logger.getLogger(ExceptionController.class).error(
				"Catching an exception", e);

		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.newDocument();

			Entry entry = new Entry();
			entry.setTitle(e.getClass().getSimpleName());
			entry.setUpdated(Calendar.getInstance());
			entry.setID(request.getLocation());
			entry.setSummary(new Text(e.getMessage()));
			entry.setContent(Content.text(String.valueOf(e.getMessage())));
			entry.addAuthor(new Author(service.getTitle()));

			/**
			 * Get the stack trace ready, if necessary.
			 */
			if (doStackTrace != null && doStackTrace) {
				Element div = document.createElement("div");
				div.setAttribute("xmlns", XMLController.getXHTMLNamespace());

				Element ol = document.createElement("ol");

				StackTraceElement[] stackTraceElements = e.getStackTrace();
				for (StackTraceElement stackTraceElement : stackTraceElements) {
					Element li = document.createElement("li");
					li.appendChild(document.createTextNode(stackTraceElement
							.toString()));
					ol.appendChild(li);
				}

				div.appendChild(ol);

				entry.setContent(Content.xhtml(div));
			}

			AtomView.process(c, entry);
		} catch (Exception ee) {
		}
	}

}
