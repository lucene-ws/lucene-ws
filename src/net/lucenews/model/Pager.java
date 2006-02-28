package net.lucenews.model;

import net.lucenews.*;
import net.lucenews.model.event.*;
import net.lucenews.model.exception.*;



public class Pager extends Limiter
{

	public static final int DEFAULT_ENTRIES_PER_PAGE = 20;
	
	
	
	private Integer m_currentPage, m_entriesPerPage, m_totalEntries;
	
	
	
	public Pager ()
	{
		this( 1, DEFAULT_ENTRIES_PER_PAGE, 0 );
	}
	
	

	public Pager (int currentPage)
	{
		this( new Integer( currentPage ) );
	}
	
	
	
	public Pager (Integer currentPage)
	{
		this( currentPage, new Integer( DEFAULT_ENTRIES_PER_PAGE ) );
	}
	
	
	
	public Pager (int currentPage, int entriesPerPage)
	{
		this( new Integer( currentPage ), new Integer( entriesPerPage ) );
	}
	
	
	
	public Pager (Integer currentPage, Integer entriesPerPage)
	{
		this( currentPage, entriesPerPage, null );
	}
	
	
	
	public Pager (int currentPage, int entriesPerPage, int totalEntries)
	{
		this( new Integer( currentPage ), new Integer( entriesPerPage ), new Integer( totalEntries ) );
	}
	
	
	
	public Pager (Integer currentPage, Integer entriesPerPage, Integer totalEntries)
	{
		this.m_currentPage    = currentPage;
		this.m_entriesPerPage = entriesPerPage;
		this.m_totalEntries   = totalEntries;
	}
	
	
	
	public Integer getTotalEntries ()
	{
		if( m_totalEntries == null )
			return 0;
		return m_totalEntries;
	}
	
	
	
	public void setTotalEntries (Integer totalEntries)
	{
		this.m_totalEntries = totalEntries;
	}
	
	
	
	public Integer getEntriesPerPage ()
	{
		return m_entriesPerPage;
	}
	
	
	
	public void setEntriesPerPage (Integer entriesPerPage)
	{
		this.m_entriesPerPage = entriesPerPage;
	}
	
	
	
	public Integer getCurrentPage ()
	{
		return m_currentPage;
	}
	
	
	
	public void setCurrentPage (Integer currentPage)
	{
		this.m_currentPage = currentPage;
	}
	
	
	
	public boolean hasSufficientData ()
	{
		return getCurrentPage() != null && getEntriesPerPage() != null && getTotalEntries() != null;
	}
	
	
	
	public boolean isCurrentPageValid ()
	{
		try
		{
			int firstPage   = getFirstPage();
			int currentPage = getCurrentPage();
			int lastPage    = getLastPage();
			
			return firstPage <= currentPage && currentPage <= lastPage;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	
	
	
	public Integer getEntriesOnThisPage ()
		throws InsufficientDataException
	{
		if( getTotalEntries() == null )
			return null;
		
		if( getTotalEntries() == 0 )
			return 0;
		
		return getLast() - getFirst() + 1;
	}
	
	
	
	public Integer getLimit ()
	{
		if( getEntriesPerPage() == null )
			return null;
		return getEntriesPerPage().intValue();
	}
	
	
	
	public Integer getFirstPage ()
	{
		if( getTotalEntries() == null )
			return null;
		
		if( getTotalEntries().intValue() > 0 )
			return 1;
		
		return null;
	}
	
	
	
	public Integer getLastPage ()
	{
		if( getTotalEntries() == null || getTotalEntries().intValue() == 0 || getEntriesPerPage() == null )
			return null;
		
		if( getEntriesPerPage().intValue() == 0 )
			return null;
		
		int totalEntries   = getTotalEntries();
		int entriesPerPage = getEntriesPerPage();
		
		return new Integer( (int) Math.ceil( (double) totalEntries / entriesPerPage ) );
	}
	
	
	
	public Integer getFirst () throws InsufficientDataException
	{
		dataSufficiencyCheck();
		
		if( getTotalEntries() == null || getEntriesPerPage() == null )
			return null;
		
		if( getEntriesPerPage().intValue() == 0 )
			return null;
		
		if( getTotalEntries().intValue() == 0 )
			return null;
		
		if( getCurrentPage().intValue() > getTotalPages().intValue() )
			return null;
		
		return ( getCurrentPage() - 1) * getEntriesPerPage() + 1;
	}
	
	
	
	public Integer getOffset ()
		throws InsufficientDataException
	{
		if( getFirst() == null )
			return null;
		return getFirst() - 1;
	}
	
	
	
	public Integer getLast () throws InsufficientDataException
	{
		dataSufficiencyCheck();
		
		if( getTotalEntries() == null )
			return null;
		
		if( getCurrentPage() == null || getLastPage() == null )
			return null;
		
		if( getCurrentPage().intValue() == getLastPage().intValue() )
			return getTotalEntries();
		
		if( getCurrentPage().intValue() > getTotalPages().intValue() )
			return null;
		
		return getCurrentPage() * getEntriesPerPage();
	}
	
	
	
	public Integer getPreviousPage ()
	{
		if( getTotalEntries() == null || getFirstPage() == null || getLastPage() == null )
			return null;
		
		int firstPage   = getFirstPage();
		int currentPage = getCurrentPage();
		int lastPage    = getLastPage();
		
		if( firstPage < currentPage && currentPage <= lastPage )
			return getCurrentPage() - 1;
		
		return null;
	}
	
	
	
	public Integer getNextPage ()
	{
		if( getFirstPage() == null || getCurrentPage() == null || getLastPage() == null )
			return null;
		
		int firstPage   = getFirstPage();
		int currentPage = getCurrentPage();
		int lastPage    = getLastPage();
		
		if( firstPage <= currentPage && currentPage < lastPage )
			return currentPage + 1;
		
		return null;
	}
	
	
	
	public Integer getTotalPages ()
	{
		if( getTotalEntries() == null || getEntriesPerPage() == null || getEntriesPerPage() == 0 )
			return null;
		
		int totalEntries   = getTotalEntries();
		int entriesPerPage = getEntriesPerPage();
		
		return new Integer( (int) Math.ceil( (double) totalEntries / entriesPerPage ) );
	}
	
	
	
	public String toString () {
		String buffer = "" + getCurrentPage();
		if( getLastPage() != -1 )
			buffer += ":" + getTotalEntries();
		if( getEntriesPerPage() != -1 )
			buffer += "[" + getEntriesPerPage() + "]";
		return buffer;
	}
	
	
	
}
