package net.lucenews.model;

import net.lucenews.*;
import net.lucenews.model.event.*;
import net.lucenews.model.exception.*;

import java.util.*;
import java.io.IOException;
import java.util.Iterator;
import org.apache.lucene.search.Hits;

public class HitsIterator implements Iterator<LuceneDocument>
{
	
	private Hits hits;
	private int first, last, cursor;
	private int total;
	private Limiter limiter;
	
	private LuceneIndex index;
	
	
	
	public HitsIterator (Hits hits)
	{
		this( hits, null );
	}
	
	public HitsIterator (Hits hits, Limiter limiter)
	{
		this( hits, limiter, null );
	}
	
	public HitsIterator (Hits hits, Limiter limiter, LuceneIndex index)
	{
		this.hits  = hits;
		this.index = index;
		this.limiter = limiter;
		
		if( limiter == null )
		{
			first = 1;
			last  = hits.length();
		}
		else
		{
			limiter.setTotalEntries( hits.length() );
			
			if( limiter.getFirst() == null || limiter.getLast() == null )
			{
				first = 0;
				last  = -1;
			}
			else
			{
				first = limiter.getFirst();
				last  = limiter.getLast();
			}
		}
		
		reset();
		total = last - first + 1;
	}
	
	
	public Limiter getLimiter ()
	{
		return limiter;
	}
	
	public int cursor ()
	{
		return cursor;
	}
	
	
	public int id ()
		throws IOException
	{
		return hits.id( cursor - 1 );
	}
	
	public float score ()
		throws IOException
	{
		return hits.score( cursor - 1 );
	}
	
	public LuceneDocument document ()
		throws IOException
	{
		LuceneDocument document = new LuceneDocument( hits.doc( cursor ) );
		
		//if( index != null )
		//	document.setIndex( index );
		
		return document;
	}
	
	
	public void reset ()
	{
		cursor = first - 1;
	}
	
	
	
	public boolean hasNext ()
	{
		if( total < 1 )
			return false;
		
		if( total == 1 )
			return first == ( cursor + 1 );
		
		return first <= (cursor + 1) && (cursor + 1) <= last;
	}
	
	public LuceneDocument next ()
	{
		try
		{
			LuceneDocument document = new LuceneDocument( hits.doc( cursor++ ) );
			
			//if( index != null )
			//	document.setIndex( index );
			
			return document;
		}
		catch(IndexOutOfBoundsException ioobe)
		{
			return null;
		}
		catch(IOException ioe)
		{
			return null;
		}
	}
	
	
	
	public boolean hasPrevious ()
	{
		if( total < 1 )
			return false;
		return first <= (cursor - 1) && (cursor - 1) <= last;
	}
	
	public LuceneDocument previous ()
	{
		try
		{
			return new LuceneDocument( hits.doc( --cursor ) );
		}
		catch(IOException ioe)
		{
			return null;
		}
	}
	
	
	
	public void remove ()
	{
	}
	
}
