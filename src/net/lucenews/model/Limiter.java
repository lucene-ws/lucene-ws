package net.lucenews.model;

import net.lucenews.*;
import net.lucenews.model.event.*;
import net.lucenews.model.exception.*;

public class Limiter
{
	
	private Integer m_limit;
	private Integer m_offset;
	private Integer m_totalEntries;
	
	public Limiter ()
	{
		this( null, null, null );
	}
	
	public Limiter (Integer limit)
	{
		this( limit, null, null );
	}
	
	public Limiter (Integer limit, Integer offset)
	{
		this( limit, offset, null );
	}
	
	public Limiter (Integer limit, Integer offset, Integer totalEntries)
	{
		m_limit   = limit;
		m_offset  = offset;
		m_totalEntries = totalEntries;
	}
	
	
	public Integer getLimit ()
	{
		return m_limit;
	}
	
	public void setLimit (Integer limit)
	{
		m_limit = limit;
	}
	
	
	
	public Integer getOffset ()
	{
		if( m_offset == null )
			return 0;
		return m_offset;
	}
	
	public void setOffset (Integer offset)
	{
		m_offset = offset;
	}
	
	
	
	public Integer getTotalEntries ()
	{
		return m_totalEntries;
	}
	
	public void setTotalEntries (Integer totalEntries)
	{
		m_totalEntries = totalEntries;
	}
	
	
	
	public boolean isValid ()
	{
		return getLimit() != null && getTotalEntries() != null && getTotalEntries().intValue() > 0;
	}
	
	public boolean hasSufficientData ()
	{
		return getLimit() != null;
	}
	
	protected void dataSufficiencyCheck () throws InsufficientDataException
	{
		if( !hasSufficientData() )
			throw new InsufficientDataException( "Insufficient data to calculate pager property" );
	}
	
	
	
	public Integer getFirst () throws InsufficientDataException
	{
		dataSufficiencyCheck();
		
		int limit  = getLimit();
		int offset = getOffset();
		int totalEntries = getTotalEntries();
		
		int first = offset + 1;
		int last  = first + limit - 1;
		
		int firstEntry = 1;
		int lastEntry  = totalEntries;
		
		if( firstEntry <= first && first <= lastEntry )
			return first;
		
		return null;
	}
	
	
	
	public Integer getLast () throws InsufficientDataException
	{
		dataSufficiencyCheck();
		
		int limit  = getLimit();
		int offset = getOffset();
		int totalEntries = getTotalEntries();
		
		int first = offset + 1;
		int last  = first + limit - 1;
		
		int firstEntry = 1;
		int lastEntry  = totalEntries;
		
		if( firstEntry <= first && first <= lastEntry )
			if( last <= lastEntry )
				return last;
			else
				return lastEntry;
		
		return null;
	}
	
	
	public int getSkipped () throws InsufficientDataException
	{
		dataSufficiencyCheck();
		if( getFirst() == null )
			return 0;
		return getFirst() - 1;
	}
	
	
	
	
}
