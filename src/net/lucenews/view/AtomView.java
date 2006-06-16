package net.lucenews.view;

import java.io.*;
import net.lucenews.atom.*;
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



public class AtomView extends View {
    
    
    public static void process (LuceneContext c)
        throws ParserConfigurationException, TransformerException, IOException
    {
        if (c.stash().get("atom_introspection_document") instanceof IntrospectionDocument) {
            process( c, (IntrospectionDocument) c.stash().get("atom_introspection_document") );
        }
        
        if (c.stash().get("atom_feed") instanceof Feed) {
            process( c, (Feed) c.stash().get("atom_feed") );
        }
    }
    
    
    
    public static void process (LuceneContext c, IntrospectionDocument introspectionDocument)
        throws ParserConfigurationException, TransformerException, IOException
    {
        XMLView.process( c, introspectionDocument.asDocument() );
    }
    
    
    
    public static void process (LuceneContext c, Feed feed)
        throws ParserConfigurationException, TransformerException, IOException
    {
        XMLView.process( c, feed.asDocument() );
    }
    
    
    
    public static void process (LuceneContext c, Entry entry)
        throws ParserConfigurationException, TransformerException, IOException
    {
        XMLView.process( c, entry.asDocument() );
    }
    
    
}
