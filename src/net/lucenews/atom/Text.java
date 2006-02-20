package net.lucenews.atom;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Text
{
	
	private String m_text;
	
	
	
	public Text (String text)
	{
		setText( text );
	}
	
	
	
	public String getText ()
	{
		return m_text;
	}
	
	public void setText (String text)
	{
		m_text = text;
	}
	
	
	public Element asElement (Document document, String tagName)
	{
		Element text = document.createElement( tagName );
		
		text.appendChild( document.createTextNode( String.valueOf( getText() ) ) );
		
		return text;
	}
	
}
