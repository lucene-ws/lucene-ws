package net.lucenews.atom;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Text
{
	
	private String m_text;
	private String m_type;
	
	
	
	public Text (String text)
	{
		setText( text );
	}
	
	public Text (String text, String type)
	{
        setText( text );
        setType( type );
	}
	
	
	
	public String getText ()
	{
		return m_text;
	}
	
	public void setText (String text)
	{
		m_text = text;
	}
	
	public String getType () {
        return m_type;
	}
	
	public void setType (String type) {
        m_type = type;
	}
	
	public String toString () {
        return getText();
	}
	
	public Element asElement (Document document, String tagName)
	{
		Element text = document.createElement( tagName );
		
		text.appendChild( document.createTextNode( String.valueOf( getText() ) ) );
		
		return text;
	}
	
    public static Text parse (Element element)
        throws AtomParseException
    {
        String type = element.getAttribute( "type" );
        
        if( type == null || type.trim().length() == 0 )
            type = "text";
        
        type = type.toLowerCase();
        
        if( !type.equals("text") || !type.equals("html") || !type.equals("xhtml") )
            throw new AtomParseException( "Unknown text type: \"" + type + "\"" );
        
        return null;
    }
	
}
