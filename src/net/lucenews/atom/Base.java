package net.lucenews.atom;

import java.text.*;
import java.util.*;

public abstract class Base
{
	// Required
	private String m_id;
	private String m_title;
	private String m_updated;
	// Recommended
	private List<Author> m_authors;
	private List<Link>   m_links;
	// Optional
	private Category          m_category;
	private List<Contributor> m_contributors;
	
	
	public Base ()
	{
		m_links        = new LinkedList<Link>();
		m_authors      = new LinkedList<Author>();
		m_contributors = new LinkedList<Contributor>();
	}
	
	
	public String getID ()
	{
		return m_id;
	}
	
	public void setID (String id)
	{
		m_id = id;
	}
	
	
	
	public String getTitle ()
	{
		return m_title;
	}
	
	public void setTitle (String title)
	{
		m_title = title;
	}
	
	
	
	public String getUpdated ()
	{
		return m_updated;
	}
	
	public void setUpdated (String updated)
	{
		m_updated = updated;
	}
	
	public void setUpdated (Calendar calendar)
	{
		setUpdated( calendar.getTime(), calendar.getTimeZone() );
	}
	
	public void setUpdated (Date date)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime( date );
		setUpdated( calendar );
	}
	
	public void setUpdated (Date date, TimeZone timezone)
	{
		SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss" );
		
		StringBuffer buffer = new StringBuffer();
		
		formatter.format( date, buffer, new FieldPosition( DateFormat.TIMEZONE_FIELD ) );
		
		if( timezone == null )
			buffer.append( "Z" );
		else
		{
			int totalMinutes = timezone.getRawOffset() / 60000;
			
			int hours   = totalMinutes / 60;
			int minutes = totalMinutes - hours * 60;
			String h = Math.abs( hours ) >= 10 ? String.valueOf( Math.abs( hours ) ) : "0" + Math.abs( hours );
			String m = minutes >= 10 ? String.valueOf( minutes ) : "0" + minutes;
			
			if( hours < 0 )
				buffer.append( "-" );
			else
				buffer.append( "+" );
			
			buffer.append( h + ":" + m );
		}
		
		m_updated = String.valueOf( buffer );
	}



	public List<Author> getAuthors ()
	{
		return m_authors;
	}
	
	public void addAuthor (Author author)
	{
		m_authors.add( author );
	}
	
	
	
	public List<Contributor> getContributors ()
	{
		return m_contributors;
	}
	
	public void addContributor (Contributor contributor)
	{
		m_contributors.add( contributor );
	}
	
	
	
	
	public List<Link> getLinks ()
	{
		return m_links;
	}
	
	public void addLink (Link link)
	{
		m_links.add( link );
	}
	
	
	
	public Category getCategory ()
	{
		return m_category;
	}
	
	public void setCategory (Category category)
	{
		m_category = category;
	}
	
}
