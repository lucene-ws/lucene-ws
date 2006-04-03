package net.lucenews.controller;

import java.util.*;
import javax.xml.parsers.*;
import net.lucenews.*;
import net.lucenews.atom.*;
import net.lucenews.model.*;
import net.lucenews.model.exception.*;
import org.apache.lucene.document.Field;
import org.w3c.dom.*;



public class XOXOController
{
	
	
	
	/**
	 * Transforms a set of properties into an XOXO-formatted
	 * &lt;dt&gt; list.
	 * 
	 * @param properties The properties to transform
	 * @param document The DOM document used to generate elements
	 * @return An XOXO-formatted &lt;dl&gt; element
	 */
	
	public static Element asElement (Properties properties, Document document)
	{
		Element dl = document.createElement( "dl" );
		dl.setAttribute( "class", "xoxo" );
		
		Enumeration names = properties.propertyNames();
		while( names.hasMoreElements() )
		{
			String name = (String) names.nextElement();
			String value = properties.getProperty( name );
		
			Element dt = document.createElement( "dt" );
			dt.appendChild( document.createTextNode( String.valueOf( name ) ) );
			dl.appendChild( dt );
		
			Element dd = document.createElement( "dd" );
			dd.appendChild( document.createTextNode( String.valueOf( value ) ) );
			dl.appendChild( dd );
		}
	
		return dl;
	}
	
	
	
	
	/**
	 * Transforms the given document into an XOXO-formatted
	 * &lt;dl&gt; list.
	 * 
	 * @param luceneDocument The document to be transformed
	 * @param document the DOM document used to create elements
	 * @return An XOXO-formatted &lt;dl&gt; element
	 */
	
	public static Element asElement (LuceneDocument luceneDocument, Document document)
	{
		Element dl = document.createElement( "dl" );
		dl.setAttribute( "class", "xoxo" );
		
		Enumeration<Field> fields = luceneDocument.fields();
		while( fields.hasMoreElements() )
		{
			Field field = fields.nextElement();
			
			String name  = field.name();
			String value = field.stringValue();
		
			Element dt = document.createElement( "dt" );
			
			String className = getClassName( field );
			
			if( className.length() > 0 )
				dt.setAttribute( "class", className );
			
			dt.appendChild( document.createTextNode( String.valueOf( name ) ) );
			dl.appendChild( dt );
		
			Element dd = document.createElement( "dd" );
			dd.appendChild( document.createTextNode( String.valueOf( value ) ) );
			dl.appendChild( dd );
		}
	
		return dl;
	}
	
	
	
	/**
	 * Determines the appropriate class name for a given field.
	 * 
	 * @param field The field in question
	 * @return The appropriate class name for the given field
	 */
	
	public static String getClassName (Field field)
	{
		StringBuffer classBuffer = new StringBuffer();
			
		if( field.isStored() )
			classBuffer.append( " stored" );
			
		if( field.isIndexed() )
			classBuffer.append( " indexed" );
			
		if( field.isTokenized() )
			classBuffer.append( " tokenized" );
			
		if( field.isTermVectorStored() )
			classBuffer.append( " termvectorstored" );
			
		return String.valueOf( classBuffer ).trim();
	}
	
	
	
	/**
	 * Transforms the given Atom entry into an XOXO-formatted
	 * &lt;dl&gt; list element. This is typically accomplished by
	 * examining the entry's content.
	 * 
	 * @param entry The Atom entry to transform
	 * @return An XOXO-formatted &lt;dl&gt; list element.
	 */
	
	public static Element asElement (Entry entry)
		throws LuceneException
	{
		Content content = entry.getContent();
		
		System.err.println( "HERE, Content = " + content.getClass() );
		
		if( content == null )
			throw new LuceneException( "Entry contains no content", LuceneResponse.SC_BAD_REQUEST );
		
		if( content instanceof NodeContent ) {
            NodeContent nodeContent = (NodeContent) content;
            Node[] nodes = nodeContent.getNodes();
            
            for( int i = 0; i < nodes.length; i++ ) {
                Node node = nodes[ i ];
                if( node.getNodeType() == Node.ELEMENT_NODE ) {
                    Element dl = (Element) ( (Element) node ).getElementsByTagName( "dl" ).item( 0 );
                    System.err.println( "<dl>: " + dl );
                    return dl;
                }
            }
		}
		
		if( content instanceof TextContent ) {
            TextContent textContent = (TextContent) content;
            System.err.println( "Type: " + textContent.getType() );
            if( textContent.isType("xhtml") ) {
                System.err.println("IT IS XHTML");
                try {
                    return (Element) textContent.asDocument().getElementsByTagName( "dl" ).item( 0 );
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
            }
		}
		
		return null;
	}
	
	
	
	/**
	 * Transforms a given Atom entry into an array
	 * of fields.
	 * 
	 * @param entry The Atom entry to transform
	 * @return An array of fields
	 * @throws LuceneException
	 */
	
	public static Field[] asFields (Entry entry)
		throws LuceneException
	{
		return asFields( asElement( entry ) );
	}
	
	
	
	
	
	/**
	 * Transforms a given Atom entry into a set
	 * of properties.
	 * 
	 * @param entry The Atom entry to transform
	 * @return A set of properties
	 * @throws LuceneException
	 */
	
	public static Properties asProperties (Entry entry)
		throws LuceneException
	{
		return asProperties( asElement( entry ) );	
	}
	
	
	
	
	
	/**
	 * Transforms the given properties into an Atom content
	 * object.
	 * 
	 * @param properties The properties to be transformed
	 * @return An Atom content object containing the properties
	 * @throws ParserConfigurationException
	 */
	
	public static Content asContent (Properties properties)
		throws ParserConfigurationException
	{
		Document document = XMLController.newDocument();
		
		Element div = document.createElement( "div" );
		div.setAttribute( "xmlns", XMLController.getXHTMLNamespace() );
		
		div.appendChild( asElement( properties, document ) );
		
		return Content.xhtml( div );
	}
	
	
	
	/**
	 * Parses XOXO formatted XHTML into a Properties object.
	 * 
	 * @param dl A DOM element corresponding to the &lt;dl&gt; list
	 * @return a set of properties
	 * @throws LuceneException
	 */
	
	public static Properties asProperties (Element dl)
		throws LuceneException
	{
		Properties properties = new Properties();
		
		Field[] fields = asFields( dl );
		for( int i = 0; i < fields.length; i++ )
		{
			Field field = fields[ i ];
			properties.setProperty( field.name(), field.stringValue() );
		}
		
		return properties;
	}

	
	
	/**
	 * Transforms a given &lt;dl&gt; list element into an array
	 * of fields.
	 * 
	 * @param dl The &lt;dl&gt; list element
	 * @return An array of fields
	 * @throws LuceneException
	 */
	
	public static Field[] asFields (Element dl)
		throws LuceneException
	{
		char state = 't'; // collecting <dt> initially, will switch between 't' and 'd' (<dd>)
		
		List<Field> fields = new LinkedList<Field>();
		
		
		String name  = null;
		String value = null;
		boolean indexed = false;
		boolean stored  = false;
		boolean tokenized = false;
		boolean termVectorStored = false;
		
		
		NodeList nodes = dl.getChildNodes();
		for( int i = 0; i < nodes.getLength(); i++ )
		{
			Node node = nodes.item( i );
			
			switch( node.getNodeType() )
			{
				case Node.ELEMENT_NODE:
					Element element = (Element) node;
					
					if( element.getTagName().equals( "dt" ) )
					{
						if( state == 't' )
						{
							name = element.getFirstChild().getNodeValue();
							
							indexed = false;
							stored  = false;
							tokenized = false;
							termVectorStored = false;
							
							String classString = element.getAttribute( "class" );
							if( classString != null )
							{
								String[] classes = classString.split( " " );
								for( int j = 0; j < classes.length; j++ )
								{
									String _class = classes[ j ];
									
									if( _class.equals( "indexed" ) )
										indexed = true;
									
									if( _class.equals( "stored" ) )
										stored = true;
									
									if( _class.equals( "tokenized" ) )
										tokenized = true;
									
									if( _class.toLowerCase().equals( "termvectorstored" ) )
										termVectorStored = true;
								}
							}
							
							state = 'd';
						}
						else
						{
							//properties.setProperty( name, value );
							fields.add( asField( name, value, indexed, stored, tokenized, termVectorStored ) );
							state = 't';
						}
					}
					
					if( element.getTagName().equals( "dd" ) )
					{
						if( state == 'd' )
						{
							if( element.getFirstChild() == null )
								value = null;
							else
								value = element.getFirstChild().getNodeValue();
							
							if( name != null && value != null )
							{
								fields.add( asField( name, value, stored, indexed, tokenized, termVectorStored ) );
							}
							value = null;
							state = 't';
						}
						else
						{
						}
					}
					
					break;
			}
		}
		
		return fields.toArray( new Field[]{} );
	}
	
	
	
	
	/**
	 * Produces a new Field object given the standard field constructor
	 * parameters. If the requested field is not stored, not indexed and
	 * not tokenized (a dud, so to speak), a field as constructed by 
	 * {@link org.apache.lucene.document.Field#Text} will be returned.
	 * 
	 * @param name
	 * @param value
	 * @param stored
	 * @param indexed
	 * @param tokenized
	 * @param termVectorStored
	 * @return a field
	 */
	
	protected static Field asField (String name, String value, boolean stored, boolean indexed, boolean tokenized, boolean termVectorStored)
	{
		if( !stored && !indexed && !tokenized )
			return Field.Text( name, value, termVectorStored );
		else
			return new Field( name, value, stored, indexed, tokenized, termVectorStored );
	}
	
	
}
