package net.lucenews.model;

import java.io.*;
import java.util.*;
import net.lucenews.model.exception.*;



public class LuceneDocumentComparator implements Comparator<LuceneDocument>
{
	
	
	
	public int compare (LuceneDocument document1, LuceneDocument document2)
	{
		if( document1 == document2 )
			return 0;
		
		try
		{
			boolean reversed = true;
			
			// Compare the updated fields
			Integer updatedCompared = compareUpdated( document1, document2 );
			
			if( updatedCompared != null )
				return reversed ? -updatedCompared : updatedCompared;
			
			// Compare the identifiers
			Integer identifiersCompared = compareIdentifiers( document1, document2 );
			if( identifiersCompared != null )
				return reversed ? -identifiersCompared : identifiersCompared;
			
			// We can't seem to figure this one out
			return -1;
		}
		catch(IOException ioe)
		{
			return -1;
		}
	}
	
	
	
	protected Integer compareUpdated (LuceneDocument document1, LuceneDocument document2)
		throws IOException
	{
		LuceneIndex index1 = document1.getIndex();
		LuceneIndex index2 = document2.getIndex();
		
		if( index1 == null || index2 == null )
			return null;
		
		if( !index1.hasUpdatedField() || !index2.hasUpdatedField() )
			return null;
		
		if( !index1.hasUpdated( document1 ) && !index2.hasUpdated( document2 ) )
			return null;
		
		
		Calendar updated1 = null;
			
		try
		{
			updated1 = index1.getUpdated( document1 );
		}
		catch(InsufficientDataException ide1)
		{
		}
			
			
			
		Calendar updated2 = null;
			
		try
		{
			updated2 = index2.getUpdated( document2 );
		}
		catch(InsufficientDataException ide2)
		{
		}
			
		if( updated1 != null && updated2 != null )
			return updated1.compareTo( updated2 );
		
		if( updated1 != null )
			return 1;
		
		if( updated2 != null )
			return -1;
		
		return null;
	}
	
	
	
	protected Integer compareIdentifiers (LuceneDocument document1, LuceneDocument document2)
		throws IOException
	{
		LuceneIndex index1 = document1.getIndex();
		LuceneIndex index2 = document2.getIndex();
		
		if( index1 == null || index2 == null )
			return null;
		
		if( !index1.hasIdentifyingField() || !index2.hasIdentifyingField() )
			return null;
		
		if( !index1.isDocumentIdentified( document1 ) && !index2.isDocumentIdentified( document2 ) )
			return null;
		
		
		String identifier1 = null;
			
		try
		{
			identifier1 = index1.getIdentifier( document1 );
		}
		catch(InsufficientDataException ide1)
		{
		}
			
			
			
		String identifier2 = null;
			
		try
		{
			identifier2 = index2.getIdentifier( document2 );
		}
		catch(InsufficientDataException ide2)
		{
		}
		
		
		if( identifier1 == null && identifier2 == null )
			return null;
		
		if( identifier1 == null )
			return -1;
		
		if( identifier2 == null )
			return 1;
		
		
		
		Integer int1 = null;
		
		try
		{
			int1 = Integer.valueOf( identifier1 );
		}
		catch(NumberFormatException nfe1)
		{
		}
		
		Integer int2 = null;
		
		try
		{
			int2 = Integer.valueOf( identifier2 );
		}
		catch(NumberFormatException nfe2)
		{
		}
		
		
		if( int1 != null && int2 != null )
			return int1.compareTo( int2 );
		
		return identifier1.compareTo( identifier2 );
	}
	
	
	
	public boolean equals (LuceneDocument other)
	{
		return false;
	}
	
}
