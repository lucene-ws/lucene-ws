package net.lucenews.model.exception;

public class MultipleValueException extends RuntimeException
{
	public MultipleValueException ()
	{
		super();
	}
	
	public MultipleValueException (String message)
	{
		super( message );
	}
}
