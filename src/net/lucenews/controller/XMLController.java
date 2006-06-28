package net.lucenews.controller;

import net.lucenews.*;
import net.lucenews.atom.*;
import net.lucenews.model.*;
import net.lucenews.view.*;
import java.io.*;
import java.util.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;



public class XMLController extends Controller {
    
    /**
     * Retrieves the namespace associated with XHTML.
     * 
     * @return The namespace associated with XHTML
     */
    
    public static String getXHTMLNamespace () {
        return "http://www.w3.org/1999/xhtml";
    }
    
    
    
    /**
     * Creates a new DOM Document from the default Document Builder
     * 
     * @return A new DOM Document
     * @throws ParserConfigurationException
     */
    
    public static Document newDocument () throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.newDocument();
    }
    
    
    
    /**
     * Forms a standard acknowledgement for the user. This is
     * typically used when the action itself produces no
     * output (i.e. - optimizing an index).
     * 
     * @param c The context
     * @throws ParserConfigurationException
     * @throws TransformerException
     * @throws IOException
     */
    public static void acknowledge (LuceneContext c)
        throws
            TransformerException, ParserConfigurationException,
            TransformerException, IOException
    {
        LuceneWebService   service = c.service();
        LuceneIndexManager manager = service.getIndexManager();
        LuceneRequest      req     = c.req();
        LuceneResponse     res     = c.res();
        
        
        
        
        Entry entry = new Entry();
        
        entry.setTitle( "OK" );
        entry.setUpdated( Calendar.getInstance() );
        entry.setID( req.getLocation() );
        entry.setSummary( new Text( "OK" ) );
        
        AtomView.process( c, entry );
    }
    
    
    
    /**
     * Tidies a DOM Document.
     * 
     * @param document The DOM Document to be tidied.
     */
    public static void tidy (Document document) {
        tidy( document, document.getDocumentElement(), "" );
    }
    
    
    
    public static void tidy (Document document, NodeList nodeList, String prefix) {
        Node[] nodes = new Node[ nodeList.getLength() ];
        for (int i = 0; i < nodes.length; i++) {
            nodes[ i ] = nodeList.item( i );
        }
        
        for (int i = 0; i < nodes.length; i++) {
            Node node = nodes[ i ];
            
            switch (node.getNodeType()) {
                case Node.ELEMENT_NODE:
                    tidy( document, (Element) node, prefix );
                    break;
            }
            
            if (i == ( nodes.length - 1 ) && node instanceof Element) {
                Element parent = (Element) nodes[ 0 ].getParentNode();
                parent.appendChild( document.createTextNode( "\n" + prefix.substring(0,prefix.length()-1) ) );
            }
        }
    }
    
    
    
    public static void tidy (Document document, Element element, String prefix) {
        Node parent = element.getParentNode();
        
        NodeList children = element.getChildNodes();
        
        if (parent instanceof Element) {
            ( (Element) parent ).insertBefore( document.createTextNode( "\n" + prefix ), element );
        }
        
        tidy( document, children, prefix + "\t" );
    }
    
}
