package net.lucenews.view;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import net.lucenews.LuceneContext;
import net.lucenews.opensearch.OpenSearch;
import net.lucenews.opensearch.OpenSearchDescription;
import net.lucenews.opensearch.OpenSearchException;
import net.lucenews.opensearch.OpenSearchResponse;

public class OpenSearchView extends View {

	private static final long serialVersionUID = 6148821597570139160L;

	public static void process(LuceneContext c) throws OpenSearchException,
			ParserConfigurationException, TransformerException, IOException {
		process(c, c.getOpenSearchResponse(), c.getOpenSearchFormat());
	}

	public static void process(LuceneContext c, OpenSearchResponse response,
			OpenSearch.Format format) throws OpenSearchException,
			ParserConfigurationException, TransformerException, IOException {
		XMLView.process(c, response.asDocument(format));
	}

	public static void process(LuceneContext c, OpenSearchResponse response,
			OpenSearch.Format format, OpenSearch.Mode mode)
			throws OpenSearchException, ParserConfigurationException,
			TransformerException, IOException {
		XMLView.process(c, response.asDocument(format, mode));
	}

	public static void process(LuceneContext c,
			OpenSearchDescription description) throws OpenSearchException,
			ParserConfigurationException, TransformerException, IOException {
		c.getResponse().setContentType(
				"application/opensearchdescription+xml;charset=utf-8");
		XMLView.process(c, description.asDocument());
	}

	public static void process(LuceneContext c,
			OpenSearchDescription description, OpenSearch.Mode mode)
			throws OpenSearchException, ParserConfigurationException,
			TransformerException, IOException {
		c.getResponse().setContentType(
				"application/opensearchdescription+xml;charset=utf-8");
		XMLView.process(c, description.asDocument(mode));
	}

}
