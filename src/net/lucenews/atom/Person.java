package net.lucenews.atom;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public abstract class Person
{
    
    private String m_name, m_uri, m_email;
    
    
    
    public Person (String name)
    {
        this( name, null, null );
    }
    
    public Person (String name, String uri, String email)
    {
        setName( name );
        setURI( uri );
        setEmail( email );
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
    
    public String getEmail ()
    {
        return m_email;
    }
    
    public void setEmail (String email)
    {
        m_email = email;
    }
    
    protected String getTagName ()
    {
        return getClass().getSimpleName().toLowerCase();
    }
    
    public Element asElement (Document document)
    {
        Element person = document.createElement( getTagName() );
        
        Element name = document.createElement( "name" );
        name.appendChild( document.createTextNode( String.valueOf( getName() ) ) );
        person.appendChild( name );
        
        if( getEmail() != null )
        {
            Element email = document.createElement( "email" );
            email.appendChild( document.createTextNode( String.valueOf( getEmail() ) ) );
            person.appendChild( email );
        }
        
        if( getURI() != null )
        {
            Element uri = document.createElement( "uri" );
            uri.appendChild( document.createTextNode( String.valueOf( getURI() ) ) );
            person.appendChild( uri );
        }
        
        return person;
    }
    
}
