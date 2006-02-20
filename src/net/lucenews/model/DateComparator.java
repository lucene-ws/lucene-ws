package net.lucenews.model;

import net.lucenews.*;
import net.lucenews.model.event.*;
import net.lucenews.model.exception.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.SortComparatorSource;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.ScoreDocComparator;
import org.apache.lucene.search.SortField;

public class DateComparator implements SortComparatorSource, ScoreDocComparator
{
	private IndexReader reader;
	private String fieldName;
	private int  style;
	private Locale locale;
	private boolean reverse;
	
	public DateComparator ()
	{
		this( false );
		style = DateFormat.DEFAULT;
	}
	
	public DateComparator (boolean reverse)
	{
		this.reverse = reverse;
		style = DateFormat.DEFAULT;
	}
	
	public DateComparator (IndexReader reader, String fieldName)
	{
		this( reader, fieldName, false );
		style = DateFormat.DEFAULT;
	}
	
	public DateComparator (IndexReader reader, String fieldName, boolean reverse)
	{
		this.reader = reader;
		this.fieldName = fieldName;
		this.reverse = reverse;
		style = DateFormat.DEFAULT;
	}
	
	
	public int getStyle ()
	{
		return style;
	}
	
	public void setStyle (int style)
	{
		this.style = style;
	}
	
	public Locale getLocale ()
	{
		return locale;
	}
	
	public void setLocale (Locale locale)
	{
		this.locale = locale;
	}
	
	
	public ScoreDocComparator newComparator (IndexReader reader, String fieldName)
	{
		DateComparator dc = new DateComparator( reader, fieldName, reverse );
		
		dc.setStyle( getStyle() );
		dc.setLocale( getLocale() );
		
		return dc;
	}
	
	protected Calendar getCalendar (ScoreDoc sc)
	{
		Calendar calendar = Calendar.getInstance();
		
		try
		{
			Document document = reader.document( sc.doc );
			Field    field    = document.getField( fieldName );
			
			DateFormat format = null;
			if( getLocale() == null )
				format = DateFormat.getDateInstance( getStyle() );
			else
				format = DateFormat.getDateInstance( getStyle(), getLocale() );
			
			try
			{
				calendar.setTime( format.parse( field.stringValue() ) );
			}
			catch(Exception e)
			{
				return null;
			}
		}
		catch(IOException ioe)
		{
		}
		
		return calendar;
	}
	
	public int compare (ScoreDoc i, ScoreDoc j)
	{
		//System.err.println( "Comparing doc " + i.doc + " against doc " + j.doc );
		Calendar ic = getCalendar( i );
		Calendar jc = getCalendar( j );
		
		//System.err.println( "    Comparing " + ic + " against " + jc );
		
		int compare = 0;
		
		if( ic != null && jc != null )
			compare = ic.compareTo( jc );
		
		if( ic != null && jc == null )
			compare =  1;
		
		if( ic == null && jc != null )
			compare = -1;
		
		//System.err.println( "        RESULT: " + compare );
		
		if( reverse )
			return -compare;
		else
			return compare;
	}
	
	public int sortType ()
	{
		return SortField.CUSTOM;
	}
	
	public Comparable sortValue (ScoreDoc i)
	{
		return getCalendar( i );
	}
	
}
