package net.lucenews.atom;

import org.w3c.dom.*;
import java.util.*;

public class AtomCollection
{
	private String m_title, m_href, m_member_type, m_list_template;
	
	
	public AtomCollection (String title, String href, String member_type)
	{
		this( title, href, member_type, null );
	}
	
	public AtomCollection (String title, String href, String member_type, String list_template)
	{
		m_title = title;
		m_href = href;
		m_member_type = member_type;
		m_list_template = list_template;
	}
	
	
	public String getTitle ()
	{
		return m_title;
	}
	
	public void setTitle (String title)
	{
		m_title = title;
	}
	
	
	
	public String getHREF ()
	{
		return m_href;
	}
	
	public void setHREF (String href)
	{
		m_href = href;
	}
	
	
	
	public String getMemberType ()
	{
		return m_member_type;
	}
	
	public void setMemberType (String member_type)
	{
		m_member_type = member_type;
	}
	
	
	
	public String getListTemplate ()
	{
		return m_list_template;
	}
	
	public void setListTemplate (String list_template)
	{
		m_list_template = list_template;
	}
	
	
	
	
	public Element asElement (Document document)
	{
		Element collection = document.createElement( "collection" );
		
		collection.setAttribute( "title", String.valueOf( getTitle() ) );
		collection.setAttribute( "href", String.valueOf( getHREF() ) );
		
		Element member_type = document.createElement( "member-type" );
		member_type.appendChild( document.createTextNode( String.valueOf( getMemberType() ) ) );
		collection.appendChild( member_type );
		
		if( getListTemplate() != null )
		{
			Element list_template = document.createElement( "list-template" );
			list_template.appendChild( document.createTextNode( String.valueOf( getListTemplate() ) ) );
			collection.appendChild( list_template );
		}
		
		return collection;
	}
	
	
}
