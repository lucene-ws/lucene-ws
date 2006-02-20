package net.lucenews.atom;

public class Contributor extends Person
{
	
	public Contributor (String name)
	{
		super( name );
	}
	
	public Contributor (String name, String uri, String email)
	{
		super( name, uri, email );
	}
	
	
	
	protected String getTagName ()
	{
		return "contributor";
	}
	
}
