package net.lucenews.model.exception;

import javax.servlet.http.*;


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
	
	public int getStatus ()
	{
		return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
	}
}
