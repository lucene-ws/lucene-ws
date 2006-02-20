package net.lucenews.atom;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;


public class OpenSearchResponse extends Feed
{
	
	private Integer m_totalResults, m_startIndex, m_itemsPerPage;
	private String m_searchTerms;
	private String m_linkHREF;
	
	
	
	public Integer getTotalResults ()
	{
		return m_totalResults;
	}
	
	public void setTotalResults (Integer totalResults)
	{
		m_totalResults = totalResults;
	}
	
	
	
	
	public Integer getStartIndex ()
	{
		return m_startIndex;
	}
	
	public void setStartIndex (Integer startIndex)
	{
		m_startIndex = startIndex;
	}
	
	
	
	public Integer getItemsPerPage ()
	{
		return m_itemsPerPage;
	}
	
	public void setItemsPerPage (Integer itemsPerPage)
	{
		m_itemsPerPage = itemsPerPage;
	}
	
	
	public String getLinkHREF ()
	{
		return m_linkHREF;
	}
	
	public void setLinkHREF (String linkHREF)
	{
		m_linkHREF = linkHREF;
	}
	
	
	public String getSearchTerms ()
	{
		return m_searchTerms;
	}
	
	public void setSearchTerms (String searchTerms)
	{
		m_searchTerms = searchTerms;
	}
	
	public Document asDocument ()
		throws ParserConfigurationException, TransformerException
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.newDocument();
		
		Element feed = document.createElement( "feed" );
		feed.setAttribute( "xmlns", "http://www.w3.org/2005/Atom" );
		
		String openSearchURI = "http://a9.com/-/spec/opensearch/1.1/";
		feed.setAttribute( "xmlns:opensearch", openSearchURI );
		
		
		
		Element totalResults = document.createElementNS( openSearchURI, "opensearch:totalResults" );
		totalResults.appendChild( document.createTextNode( String.valueOf( m_totalResults ) ) );
		//feed.insertBefore( totalResults, firstEntry );
		feed.appendChild( totalResults );
		
		
		
		Element startIndex = document.createElementNS( openSearchURI, "opensearch:startIndex" );
		startIndex.appendChild( document.createTextNode( String.valueOf( m_startIndex ) ) );
		//feed.insertBefore( startIndex, firstEntry );
		feed.appendChild( startIndex );
		
		
		
		Element itemsPerPage = document.createElementNS( openSearchURI, "opensearch:itemsPerPage" );
		itemsPerPage.appendChild( document.createTextNode( String.valueOf( m_itemsPerPage ) ) );
		//feed.insertBefore( itemsPerPage, firstEntry );
		feed.appendChild( itemsPerPage );
		
		
		
		Element link = document.createElementNS( openSearchURI, "opensearch:link" );
		link.setAttribute( "rel", "search" );
		link.setAttribute( "href", String.valueOf( m_linkHREF ) );
		link.setAttribute( "type", "application/opensearchdescription+xml" );
		//feed.insertBefore( link, firstEntry );
		feed.appendChild( link );
		
		
		
		Element query = document.createElementNS( openSearchURI, "opensearch:Query" );
		query.setAttribute( "rel", "request" );
		query.setAttribute( "searchTerms", String.valueOf( m_searchTerms ) );
		//feed.insertBefore( query, firstEntry );
		feed.appendChild( query );
		
		
		addElements( document, feed );
		
		document.appendChild( feed );
		
		return document;
	}
}
