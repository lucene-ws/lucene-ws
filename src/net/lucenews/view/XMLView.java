package net.lucenews.view;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import net.lucenews.LuceneContext;
import net.lucenews.LuceneResponse;
import net.lucenews.controller.XMLController;

import org.w3c.dom.Document;

public class XMLView extends View {

	private static final long serialVersionUID = 4054086284708272060L;

	/**
	 * Displays a DOM document to response output.
	 * 
	 * @param c
	 *            The context
	 * @param document
	 *            The DOM document
	 * @throws ParserConfigurationException
	 * @throws TransformerException
	 * @throws IOException
	 */

	public static void process(LuceneContext c, Document document)
			throws ParserConfigurationException, TransformerException,
			IOException {
		LuceneResponse response = c.getResponse();

		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer transformer = factory.newTransformer();

		XMLController.tidy(document);

		transformer.transform(new DOMSource(document), new StreamResult(
				response.getWriter()));
	}

}
