package net.lucenews.model.exception;

public class DocumentsAlreadyExistException extends LuceneException
{
	
	private String[] identifiers;
	
	
	
	public DocumentsAlreadyExistException (String... identifiers)
	{
		this.identifiers = identifiers;
	}
	
	
	
	public String getMessage ()
	{
		StringBuffer buffer = new StringBuffer();
		
		if( identifiers.length == 1 )
		{
			buffer.append( "Document '" + identifiers[ 0 ] + "' already exists." );
		}
		else
		{
			buffer.append( "Documents" );
			
			for( int i = 0; i < identifiers.length; i++ )
			{
				if( i > 0 )
					if( i == identifiers.length - 1 )
						buffer.append( " and" );
					else
						buffer.append( "," );
				
				buffer.append( " '" + identifiers[ i ] + "'" );
			}
			
			buffer.append( " already exist." );
		}
		
		return String.valueOf( buffer );
	}
	
}
