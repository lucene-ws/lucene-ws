package net.lucenews.model.exception;

public class InvalidIdentifierException extends LuceneException
{
	
	private String identifier;
	
	public InvalidIdentifierException (String identifier)
	{
		this.identifier = identifier;
	}
	
	public String getMessage ()
	{
		return "Identifier '" + identifier + "' is invalid.";
	}
	
}
