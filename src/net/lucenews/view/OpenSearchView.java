package net.lucenews.view;

import java.io.*;
import net.lucenews.opensearch.*;
import net.lucenews.*;
import net.lucenews.LuceneContext;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;



public class OpenSearchView extends View {
	
	
	public static void process (LuceneContext c, OpenSearchResponse response, OpenSearch.Format format)
		throws OpenSearchException, ParserConfigurationException, TransformerException, IOException
	{
		XMLView.process(c, response.asDocument(format));
	}
	
	
	public static void process (LuceneContext c, OpenSearchResponse response, OpenSearch.Format format, OpenSearch.Mode mode)
		throws OpenSearchException, ParserConfigurationException, TransformerException, IOException
	{
		XMLView.process(c, response.asDocument(format, mode));
	}
	
	
}
