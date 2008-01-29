package net.lucenews.atom;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Generator
{
	private String m_name, m_uri, m_version;
	
	
	
	
	public Generator (String name)
	{
		this( name, null, null );
	}
	
	public Generator (String name, String uri, String version)
	{
		setName( name );
		setURI( uri );
		setVersion( version );
	}
	
	
	
	public String getName ()
	{
		return m_name;
	}
	
	public void setName (String name)
	{
		m_name = name;
	}
	
	
	
	
	public String getURI ()
	{
		return m_uri;
	}
	
	public void setURI (String uri)
	{
		m_uri = uri;
	}
	
	
	
	public String getVersion ()
	{
		return m_version;
	}
	
	public void setVersion (String version)
	{
		m_version = version;
	}
	
	
	
	
	
	public Element asElement (Document document)
	{
		Element generator = document.createElement( "generator" );
		
		if( getURI() != null )
			generator.setAttribute( "uri", getURI() );
		
		if( getVersion() != null )
			generator.setAttribute( "version", getVersion() );
		
		generator.appendChild( document.createTextNode( getName() ) );
		
		return generator;
	}
	
	
}
