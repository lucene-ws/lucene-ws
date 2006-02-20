package net.lucenews.atom;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Link
{
	
	private String m_href;
	private String m_rel;
	private String m_type;
	private String m_hreflang;
	private String m_title;
	private Integer m_length;
	
	
	
	public Link (String href)
	{
		this( href, null );
	}
	
	public Link (String href, String rel)
	{
		setHREF( href );
		setRel( rel );
	}
	
	public Link (String href, String rel, String type)
	{
		setHREF( href );
		setRel( rel );
		setType( type );
	}
	
	
	
	public static Link Alternate (String href)
	{
		return new Link( href, "alternate" );
	}
	
	public static Link Enclosure (String href)
	{
		return new Link( href, "enclosure" );
	}
	
	public static Link Related (String href)
	{
		return new Link( href, "related" );
	}
	
	public static Link Self (String href)
	{
		return new Link( href, "self" );
	}
	
	public static Link Via (String href)
	{
		return new Link( href, "via" );
	}
	
	
	
	public String getHREF ()
	{
		return m_href;
	}
	
	public void setHREF (String href)
	{
		m_href = href;
	}
	
	public String getRel ()
	{
		return m_rel;
	}
	
	public void setRel (String rel)
	{
		m_rel = rel;
	}
	
	public String getType ()
	{
		return m_type;
	}
	
	public void setType (String type)
	{
		m_type = type;
	}
	
	public String getHREFLang ()
	{
		return m_hreflang;
	}
	
	public void setHREFLang (String hreflang)
	{
		m_hreflang = hreflang;
	}
	
	public String getTitle ()
	{
		return m_title;
	}
	
	public void setTitle (String title)
	{
		m_title = title;
	}
	
	public Integer getLength ()
	{
		return m_length;
	}
	
	public void setLength (Integer length)
	{
		m_length = length;
	}
	
	
	
	public Element asElement (Document document)
	{
		Element link = document.createElement( "link" );
		
		link.setAttribute( "href", String.valueOf( getHREF() ) );
		
		if( getRel() != null )
			link.setAttribute( "rel", String.valueOf( getRel() ) );
		
		if( getType() != null )
			link.setAttribute( "type", String.valueOf( getType() ) );
		
		if( getHREFLang() != null )
			link.setAttribute( "hreflang", String.valueOf( getHREFLang() ) );
		
		if( getTitle() != null )
			link.setAttribute( "title", String.valueOf( getTitle() ) );
		
		if( getLength() != null )
			link.setAttribute( "length", String.valueOf( getLength() ) );
		
		return link;
	}
	
}
