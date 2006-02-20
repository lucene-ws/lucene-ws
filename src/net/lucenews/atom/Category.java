package net.lucenews.atom;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Category
{
	
	
	private String m_text, m_scheme, m_label;
	
	
	
	public Category (String text)
	{
		this( text, null, null );
	}
	
	public Category (String text, String scheme, String label)
	{
		setText( text );
		setScheme( scheme );
		setLabel( label );
	}
	
	
	public String getText ()
	{
		return m_text;
	}
	
	public void setText (String text)
	{
		m_text = text;
	}
	
	
	public String getScheme ()
	{
		return m_scheme;
	}
	
	public void setScheme (String scheme)
	{
		m_scheme = scheme;
	}
	
	
	public String getLabel ()
	{
		return m_label;
	}
	
	public void setLabel (String label)
	{
		m_label = label;
	}
	
	
	
	public Element asElement (Document document)
	{
		Element category = document.createElement( "category" );
		
		category.appendChild( document.createTextNode( String.valueOf( getText() ) ) );
		
		if( getScheme() != null )
			category.setAttribute( "scheme", getScheme() );
		
		if( getLabel() != null )
			category.setAttribute( "label", getLabel() );
		
		return category;
	}
	
}
