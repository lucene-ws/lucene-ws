package net.lucenews.atom;

import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Content
{
	
	private String m_type;
	private Object m_data;
	
	
	
	public Content (String type, Object data)
	{
		setType( type );
		setData( data );
	}
	
	
	
	public String getType ()
	{
		return m_type;
	}
	
	public void setType (String type)
	{
		m_type = type;
	}
	
	public Object getData ()
	{
		return m_data;
	}
	
	public void setData (Object data)
	{
		m_data = data;
	}
	
	
	public static Content src (String uri)
	{
		return new Content( "src", uri );
	}
	
	public static Content text (String text)
	{
		return new Content( "text", text );
	}
	
	public static Content html (String html)
	{
		return new Content( "html", html );
	}
	
	public static Content xhtml (Document document)
	{
		return new Content( "xhtml", document );
	}
	
	public static Content xhtml (Element element)
	{
		return new Content( "xhtml", element );
	}
	
	public static Content xhtml (NodeList nodes)
	{
		return new Content( "xhtml", nodes );
	}
	
	public static Content xml (NodeList nodes)
	{
		return new Content( "xml", nodes );
	}
	
	public static Content xml (Document document)
	{
		return new Content( "xml", document );
	}
	
	public static Content xml (Element element)
	{
		return new Content( "xml", element );
	}
	
	
	public Element asElement (Document document)
		throws TransformerException
	{
		Element content = document.createElement( "content" );
		
		if( getType().equals( "src" ) )
		{
			content.setAttribute( "type", "text/xml" );
			content.setAttribute( "src", (String) getData() );
		}
		else
		{
			content.setAttribute( "type", getType() );
			
			DOMResult result = new DOMResult( content );
			
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
			
			if( getData() instanceof Document )
				transformer.transform( new DOMSource( (Document) getData() ), result );
			
			else if( getData() instanceof Element )
				transformer.transform( new DOMSource( (Element) getData() ), result );
			
			else if( getData() instanceof NodeList )
			{
				NodeList nodes = (NodeList) getData();
				for( int i = 0; i < nodes.getLength(); i++ )
					transformer.transform( new DOMSource( nodes.item( i ) ), result );
			}
			
			else if( getData() instanceof String )
			{
				content.appendChild( document.createTextNode( (String) getData() ) );
			}
			
		}
		
		return content;
	}
	
	
	
	public static Content parse (Element element)
	{
		if( !element.getTagName().equals( "content" ) )
			return null;
		
		if( element.getAttribute( "type" ).endsWith( "xml" ) )
		{
			NodeList nodes = element.getChildNodes();
			return Content.xml( nodes );
		}
		
		if( element.getAttribute( "type" ).equals( "xhtml" ) )
		{
			Element div = (Element) element.getElementsByTagName( "div" ).item( 0 );
			return Content.xhtml( div );
		}
		
		return null;
	}
	
}
