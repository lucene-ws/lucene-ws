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
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ExceptionController extends Controller
{
	
	public static void process (LuceneContext c, Exception e)
	{
		try
		{
			process( c, e, ServletUtils.parseBoolean( c.service().getProperty( "service.debugging" ) ) );
		}
		catch(IOException ioe)
		{
		}
	}
	
	public static void process (LuceneContext c, Exception e, boolean doStackTrace)
	{
		LuceneWebService   service = c.service();
		LuceneIndexManager manager = service.getIndexManager();
		LuceneRequest      req     = c.req();
		LuceneResponse     res     = c.res();
		
		
		
		try
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.newDocument();
			
			

			
			
			
			Entry entry = new Entry();
			entry.setTitle( e.getClass().getSimpleName() );
			entry.setUpdated( Calendar.getInstance() );
			entry.setID( req.getLocation() );
			entry.setSummary( new Text( e.getMessage() ) );
			entry.setContent( Content.text( e.getMessage() ) );
			entry.addAuthor( new Author( service.getTitle() ) );
			
			
			
			
			
			
			/**
			 * Get the stack trace ready, if necessary.
			 */
			if( doStackTrace )
			{
				Element div = document.createElement( "div" );
				
				Element trace = document.createElement( "trace" );
				
				StackTraceElement[] stack = e.getStackTrace();
				for( int i = 0; i < stack.length; i++ )
				{
					Element element = document.createElement( "element" );
					element.appendChild( document.createTextNode( stack[i].toString() ) );
					trace.appendChild( element );
				}
				
				div.appendChild( trace );
				
				entry.setContent( Content.xhtml( div ) );
			}
			
			
			AtomView.process( c, entry );
		}
		catch(Exception ee)
		{
		}
	}
	
}
