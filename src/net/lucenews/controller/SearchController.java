package net.lucenews.controller;

import javax.xml.parsers.*;
import java.io.*;
import java.util.*;
import net.lucenews.atom.*;
import java.io.IOException;
import java.util.Locale;
import net.lucenews.*;
import net.lucenews.model.*;
import net.lucenews.model.exception.*;
import net.lucenews.view.*;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.search.*;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.Sort;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import javax.xml.transform.TransformerException;



public class SearchController extends Controller
{
	
	
	
	/**
	 * Performs a search.
	 * 
	 * @param c The context
	 * @throws ParserConfigurationException
	 * @throws TransformerException
	 * @throws IndicesNotFoundException
	 * @throws IOException
	 * @throws InsufficientDataException
	 * @throws ParseException
	 */
	
	public static void doGet (LuceneContext c)
		throws ParserConfigurationException, TransformerException, IndicesNotFoundException, IOException, InsufficientDataException, ParseException
	{
		LuceneWebService   service = c.service();
		LuceneIndexManager manager = service.getIndexManager();
		LuceneRequest      req     = c.req();
		LuceneIndex[]      indices = manager.getIndices( req.getIndexNames() );
		
		LuceneMultiSearcher   searcher  = null;
		IndexSearcher[] searchers = new IndexSearcher[ indices.length ];
		
		for( int i = 0; i < indices.length; i++ )
			searchers[ i ] = indices[ i ].getIndexSearcher();
		
		searcher = new LuceneMultiSearcher( searchers, getSearcherIndexField() );
		
		
		
		List<String> invalidParameterNames = new LinkedList<String>();
		
		
		String searchString = req.getSearchString();
		if( searchString == null )
			invalidParameterNames.add( req.getParameterName( LuceneKeys.SEARCH_STRING ) );
		
		
		
		/**
		 * Default field
		 */
		
		// User-specified default field
		String defaultField = req.getDefaultField();
		
		// Index-specified default field
		for( int i = 0; i < indices.length && defaultField == null; i++ )
			defaultField = indices[ i ].getDefaultField();
		
		// Service-specified default field
		if( defaultField == null )
			defaultField = service.getDefaultField();
		
		if( defaultField == null )
			invalidParameterNames.add( req.getParameterName( LuceneKeys.DEFAULT_FIELD ) );
		
		
		
		
		Analyzer analyzer = req.getAnalyzer();
		if( analyzer == null )
			invalidParameterNames.add( req.getParameterName( LuceneKeys.ANALYZER ) );
		
		
		
		if( invalidParameterNames.size() > 0 )
		{
			StringBuffer buffer = new StringBuffer();
			
			if( invalidParameterNames.size() == 1 )
			{
				buffer.append( "Valid '" + invalidParameterNames.get( 0 ) + "' parameter required." );
			}
			else
			{
				buffer.append( "Valid" );
				for( int i = 0; i < invalidParameterNames.size(); i++ )
				{
					if( i > 0 )
						if( i == ( invalidParameterNames.size() - 1 ) )
							buffer.append( " and" );
						else
							buffer.append( "," );
					
					buffer.append( " '" + invalidParameterNames.get( i ) + "'" );
				}
				
				buffer.append( " parameters required." );
			}
				
			
			throw new InsufficientDataException( String.valueOf( buffer ) );
		}
		
		
		
		Query query = null;
		
		
		
		try
		{
			query = req.getQuery();
		}
		catch(InsufficientDataException ide)
		{
			query = null;
		}
		
		
		
		if( query == null )
		{
			/**
			* Build the query parser
			*/
			
			QueryParser parser = new QueryParser( defaultField, analyzer );
			
			
			
			Locale locale = req.getLocale();
			if( locale != null )
				parser.setLocale( locale );
			
			Integer defaultOperator = req.getDefaultOperator();
			if( defaultOperator != null )
				parser.setOperator( defaultOperator );
			
			
			
			/**
			* Build the query
			*/
			
			query = parser.parse( searchString );
		}
		
		
		
		
		/**
		 * Perform the search
		 */
		
		
		
		
		Filter filter = req.getFilter();
		Sort   sort   = req.getSort();
		
		
		Hits hits = null;
		
		if( filter != null && sort != null )
			hits = searcher.search( query, filter, sort );
		
		else if( filter != null )
			hits = searcher.search( query, filter );
		
		else if( sort != null )
			hits = searcher.search( query, sort );
		
		else
			hits = searcher.search( query );
		
		
		
		Limiter limiter = req.getLimiter();
		
		HitsIterator iterator = null;
		try
		{
			iterator = new HitsIterator( hits, limiter, c.service().getIndexManager().getIndex( req.getIndexName() ) );
		}
		catch(MultipleValueException mve)
		{
			iterator = new HitsIterator( hits, limiter );
		}
		
		OpenSearchResponse response = asOpenSearchResponse( c, iterator );
		
		StringBuffer title = new StringBuffer();
		title.append( "Search results for query '" + req.getSearchString() + "'" );
		title.append( " on " + ( indices.length == 1 ? "index" : "indices" ) + " " );
		title.append( ServletUtils.joined( ServletUtils.mapped( "'[content]'", ServletUtils.objectsMapped( "getTitle", indices ) ) ) );
		
		
		
		response.setTitle( String.valueOf( title ) );
		
		AtomView.process( c, response );
	}
	
	
	
	/**
	 * Transforms the given hits iterator into an OpenSearch
	 * response.
	 * 
	 * @param c The context
	 * @param iterator The hits iterator
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws IndicesNotFoundException
	 */
	
	public static OpenSearchResponse asOpenSearchResponse (LuceneContext c, HitsIterator iterator)
		throws ParserConfigurationException, IOException, IndicesNotFoundException, InsufficientDataException
	{
		LuceneWebService   service = c.service();
		LuceneIndexManager manager = service.getIndexManager();
		LuceneRequest      req     = c.req();
		LuceneResponse     res     = c.res();
		LuceneIndex[]      indices = manager.getIndices( req.getIndexNames() );
		
		
		
		
		OpenSearchResponse response = new OpenSearchResponse();
		
		
		
		response.setLinkHREF( service.getOpenSearchDescriptionURL( req, req.getIndexNames() ) );
		response.setSearchTerms( req.getSearchString() );
		response.setUpdated( Calendar.getInstance() );
		response.addAuthor( new Author( "Lucene Web Service, version " + service.getVersion() ) );
		response.setID( req.getRequestURL() + ( ( req.getQueryString() != null ) ? "?" + req.getQueryString()  : "" ) );
		
		
		
		StringBuffer buffer = new StringBuffer();
		
		buffer.append( req.getRequestURL() );
		
		Enumeration names = req.getParameterNames();
		boolean first = true;
		while( names.hasMoreElements() )
		{
			String name = (String) names.nextElement();
			
			if( name.equals( "page" ) )
				continue;
			
			String[] values = req.getParameterValues( name );
			for( int i = 0; i < values.length; i++ )
			{
				if( first )
				{
					buffer.append( "?" );
					first = false;
				}
				else
				{
					buffer.append( "&" );
				}
				buffer.append( name + "=" + values[i] );
			}
			
		}
		
		buffer.append( first ? "?" : "&" );
		buffer.append( "page=" );
		
		String baseURL = String.valueOf( buffer );
		
		
		
		
		Limiter limiter = iterator.getLimiter();
		
		if( limiter != null && limiter instanceof Pager )
		{
			Pager pager = (Pager) limiter;
			response.setTotalResults( pager.getTotalEntries() );
			response.setStartIndex( pager.getFirst() );
			response.setItemsPerPage( pager.getEntriesPerPage() );
			
			if( pager.getCurrentPage() != null )
				response.addLink( new Link( baseURL + pager.getCurrentPage(), "self", "application/atom+xml" ) );
			
			if( pager.getFirstPage() != null )
				response.addLink( new Link( baseURL + pager.getFirstPage(), "first", "application/atom+xml" ) );
			
			if( pager.getPreviousPage() != null )
				response.addLink( new Link( baseURL + pager.getPreviousPage(), "previous", "application/atom+xml" ) );
			
			if( pager.getNextPage() != null )
				response.addLink( new Link( baseURL + pager.getNextPage(), "next", "application/atom+xml" ) );
			
			if( pager.getLastPage() != null )
				response.addLink( new Link( baseURL + pager.getLastPage(), "last", "application/atom+xml" ) );
		}
		
		iterator.reset();
		while( iterator.hasNext() )
		{
			LuceneDocument document = iterator.next();
			Integer searcherIndex = extractSearcherIndex( document );
			
			LuceneIndex index = null;
			if( searcherIndex != null )
				index = indices[ searcherIndex ];
			
			response.addEntry( DocumentController.asEntry( c, index, document, iterator.score() ) );
		}
		
		return response;
	}
	
	
	
	public static String getSearcherIndexField ()
	{
		return "lucene_ws_subSearcher";
	}
	
	
	public static Integer extractSearcherIndex (LuceneDocument document)
	{
		Integer index = null;
		
		try
		{
			index = Integer.valueOf( document.get( getSearcherIndexField() ) );
		}
		catch(NullPointerException npe)
		{
		}
		catch(NumberFormatException nfe)
		{
		}
		
		document.removeFields( getSearcherIndexField() );
		
		return index;
	}
	
	
}
