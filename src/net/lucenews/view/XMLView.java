package net.lucenews.view;

import net.lucenews.*;
import net.lucenews.controller.*;
import java.io.*;
import java.nio.charset.*;
import javax.xml.parsers.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.log4j.*;
import org.w3c.dom.*;



public class XMLView extends View {
    
    
    
    /**
     * Displays a DOM document to response output.
     * 
     * @param c The context
     * @param document The DOM document
     * @throws ParserConfigurationException
     * @throws TransformerException
     * @throws IOException
     */
    
    public static void process (LuceneContext c, Document document)
        throws ParserConfigurationException, TransformerException, IOException
    {
        LuceneRequest  request  = c.getRequest();
        LuceneResponse response = c.getResponse();
        
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer();
        
        XMLController.tidy( document );
        
        transformer.transform(
            new DOMSource( document ),
            new StreamResult( response.getWriter() )
        );
    }
    
    
    
}
