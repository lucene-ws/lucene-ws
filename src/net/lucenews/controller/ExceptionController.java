package net.lucenews.controller;

import net.lucenews.atom.*;
import net.lucenews.*;
import net.lucenews.model.*;
import net.lucenews.model.exception.*;
import net.lucenews.view.*;
import java.io.*;
import java.util.*;
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
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ExceptionController extends Controller {
    
    public static void process (LuceneContext c, Exception e) {
        try {
            process( c, e, ServletUtils.parseBoolean( c.getService().getProperty( "service.debugging" ) ) );
        }
        catch (IOException ioe) {
        }
    }
    
    public static void process (LuceneContext c, Exception e, Boolean doStackTrace) {
        LuceneWebService   service = c.getService();
        LuceneIndexManager manager = service.getIndexManager();
        LuceneRequest      req     = c.getRequest();
        LuceneResponse     res     = c.getResponse();
        
        
        Logger.getLogger(ExceptionController.class).error("Catching an exception", e);
        
        
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
            
            
            
            
            
            
            Entry entry = new Entry();
            entry.setTitle( e.getClass().getSimpleName() );
            entry.setUpdated( Calendar.getInstance() );
            entry.setID( req.getLocation() );
            entry.setSummary( new Text( e.getMessage() ) );
            entry.setContent( Content.text( String.valueOf( e.getMessage() ) ) );
            entry.addAuthor( new Author( service.getTitle() ) );
            
            
            
            
            
            
            /**
             * Get the stack trace ready, if necessary.
             */
            if ( doStackTrace != null && doStackTrace ) {
                Element div = document.createElement( "div" );
                div.setAttribute( "xmlns", XMLController.getXHTMLNamespace() );
                
                Element ol = document.createElement( "ol" );
                
                StackTraceElement[] stack = e.getStackTrace();
                for (int i = 0; i < stack.length; i++) {
                    Element li = document.createElement( "li" );
                    li.appendChild( document.createTextNode( stack[ i ].toString() ) );
                    ol.appendChild( li );
                }
                
                div.appendChild( ol );
                
                entry.setContent( Content.xhtml( div ) );
            }
            
            
            AtomView.process( c, entry );
        }
        catch (Exception ee) {
        }
    }
    
}
