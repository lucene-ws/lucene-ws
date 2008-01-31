package net.lucenews.controller;

import java.io.IOException;
import java.util.Calendar;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import net.lucenews.LuceneContext;
import net.lucenews.LuceneRequest;
import net.lucenews.LuceneResponse;
import net.lucenews.LuceneWebService;
import net.lucenews.ServletUtils;
import net.lucenews.atom.AtomParseException;
import net.lucenews.atom.Author;
import net.lucenews.atom.Entry;
import net.lucenews.model.LuceneIndexManager;
import net.lucenews.model.exception.IllegalActionException;
import net.lucenews.model.exception.IndicesNotFoundException;
import net.lucenews.model.exception.InsufficientDataException;
import net.lucenews.model.exception.LuceneException;
import net.lucenews.model.exception.MultipleValueException;
import net.lucenews.view.AtomView;

import org.xml.sax.SAXException;

public class ServicePropertiesController extends Controller {

	/**
	 * Gets the current service properties
	 * 
	 * @param c
	 *            The context
	 * @throws IndicesNotFoundException
	 * @throws ParserConfigurationException
	 * @throws TransformerException
	 * @throws IOException
	 */

	public static void doGet(LuceneContext c) throws IndicesNotFoundException,
			ParserConfigurationException, TransformerException, IOException {
		LuceneWebService service = c.getService();
		LuceneIndexManager manager = service.getIndexManager();
		LuceneRequest request = c.getRequest();
		LuceneResponse response = c.getResponse();
		Calendar lastModified = service.getPropertiesLastModified();
		String httpDate = ServletUtils.asHTTPDate(lastModified);

		if (httpDate != null) {
			response.addHeader("Etag", httpDate);
			response.addHeader("Last-Modified", httpDate);
		}

		if (!request.shouldHandle(lastModified, httpDate)) {
			response.setStatus(response.SC_NOT_MODIFIED);
			return;
		}

		Entry entry = new Entry();

		entry.setTitle(service.getTitle());
		entry.setUpdated(service.getPropertiesLastModified());
		entry.setID(service.getServicePropertiesURI(request).toString());
		entry.setContent(XOXOController.asContent(c, service.getProperties()));
		entry.addAuthor(new Author(service.getTitle()));

		AtomView.process(c, entry);
	}

	/**
	 * Updates the current service settings. Service properties will only be
	 * updated if the properties.readonly property has a false value. If it is
	 * null, it's value will be assumed to be true.
	 * 
	 * @param c
	 *            the Context
	 * @throws IllegalActionException
	 * @throws ParserConfigurationException
	 * @throws TransformerException
	 * @throws IOException
	 * @throws AtomParseException
	 */

	public static void doPut(LuceneContext c) throws IllegalActionException,
			LuceneException, SAXException, ParserConfigurationException,
			TransformerException, IOException, AtomParseException {
		LuceneWebService service = c.getService();
		LuceneIndexManager manager = service.getIndexManager();
		LuceneRequest request = c.getRequest();
		LuceneResponse response = c.getResponse();

		boolean readOnly = true;

		String _readOnly = service.getProperty("properties.readonly");
		if (_readOnly != null) {
			readOnly = ServletUtils.parseBoolean(_readOnly);
		}

		if (readOnly) {
			throw new IllegalActionException(
					"Service properties cannot be updated");
		}

		Entry[] entries = request.getEntries();

		if (entries.length == 0) {
			throw new InsufficientDataException(
					"No set of properties submitted");
		}

		if (entries.length > 1) {
			throw new MultipleValueException(
					"Many sets of properties submitted");
		}

		Entry entry = entries[0];

		Properties properties = XOXOController.asProperties(c, entry);

		service.setProperties(properties);

		response.addHeader("Location", service.getServicePropertiesURI(request)
				.toString());

		XMLController.acknowledge(c);
	}

	/**
	 * Adds to the current service settings. Service properties will only be
	 * updated if the properties.readonly property has a false value. If it is
	 * null, it's value will be assumed to be true.
	 * 
	 * @param c
	 *            the Context
	 * @throws IllegalActionException
	 * @throws ParserConfigurationException
	 * @throws TransformerException
	 * @throws IOException
	 * @throws AtomParseException
	 */

	public static void doPost(LuceneContext c) throws IllegalActionException,
			LuceneException, SAXException, ParserConfigurationException,
			TransformerException, IOException, AtomParseException {
		LuceneWebService service = c.getService();
		LuceneIndexManager manager = service.getIndexManager();
		LuceneRequest request = c.getRequest();
		LuceneResponse response = c.getResponse();

		boolean readOnly = true;

		String _readOnly = service.getProperty("properties.readonly");
		if (_readOnly != null) {
			readOnly = ServletUtils.parseBoolean(_readOnly);
		}

		if (readOnly) {
			throw new IllegalActionException(
					"Service properties cannot be added to");
		}

		Entry[] entries = request.getEntries();

		if (entries.length == 0) {
			throw new InsufficientDataException(
					"No set of properties submitted");
		}

		if (entries.length > 1) {
			throw new MultipleValueException(
					"Many sets of properties submitted");
		}

		Entry entry = entries[0];

		Properties properties = XOXOController.asProperties(c, entry);

		service.addProperties(properties);

		response.addHeader("Location", service.getServicePropertiesURI(request)
				.toString());

		XMLController.acknowledge(c);
	}

}
