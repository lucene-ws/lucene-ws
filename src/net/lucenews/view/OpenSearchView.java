package net.lucenews.view;

import java.io.*;
import java.nio.charset.*;
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
import org.apache.log4j.*;
import org.w3c.dom.Document;



public class OpenSearchView extends View {
    
    
    public static void process (LuceneContext c)
        throws OpenSearchException, ParserConfigurationException, TransformerException, IOException
    {
        process( c, c.getOpenSearchResponse(), c.getOpenSearchFormat() );
    }
    
    
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
    
    
    public static void process (LuceneContext c, OpenSearchDescription description)
        throws OpenSearchException, ParserConfigurationException, TransformerException, IOException
    {
        c.res().setContentType("application/opensearchdescription+xml;charset=utf-8");
        XMLView.process(c, description.asDocument());
    }
    
    public static void process (LuceneContext c, OpenSearchDescription description, OpenSearch.Mode mode)
        throws
            OpenSearchException, ParserConfigurationException, TransformerException, IOException
    {
        c.res().setContentType("application/opensearchdescription+xml;charset=utf-8");
        XMLView.process(c, description.asDocument(mode));
    }
    
}
