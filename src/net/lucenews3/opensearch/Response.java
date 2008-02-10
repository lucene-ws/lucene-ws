package net.lucenews3.opensearch;

import java.util.Calendar;

import net.lucenews.atom.Link;

public interface Response {

	public String getTitle();

	public void setTitle(String title);

	public String getDescription();

	public void setDescription(String description);

	public String getId();

	public void setId(String id);

	public Calendar getUpdated();

	public void setUpdated(Calendar updated);

	/**
	 * totalResults - the maximum number of results available for these search
	 * terms
	 * 
	 * Restrictions: An integer greater than or equal to 0. Default: The number
	 * of items that were returned in this set of results. Requirements: May
	 * appear zero or one time.
	 */

	public Integer getTotalResults();

	public void setTotalResults(Integer totalResults);

	/**
	 * startIndex - the index of the first item returned in the result.
	 * 
	 * Restrictions: An integer greater than or equal to 1. Note: The first
	 * result is 1. Default: 1 Requirements: May appear zero or one time.
	 */

	public Integer getStartIndex();

	public void setStartIndex(Integer startIndex);

	/**
	 * itemsPerPage - the maximum number of items that can appear in one page of
	 * results.
	 * 
	 * Restrictions: An integer greater than or equal to 1. Default: The number
	 * of items that were returned in this set of results. Requirements: May
	 * appear zero or one time.
	 */

	public Integer getItemsPerPage();

	public void setItemsPerPage(Integer itemsPerPage);

	/**
	 * link - a reference back to the OpenSearch Description file
	 * 
	 * Attributes: This is a clone of the link element in Atom, including href,
	 * hreflang, rel, and type attributes. Restrictions: The rel attribute must
	 * equal search. Note: New in version 1.1. Requirements: May appear zero or
	 * one time.
	 */

	public Link getLink();

	public void setLink(Link link);

	public LinkList getLinks();
	
	public void setLinks(LinkList links);

	/**
	 * Query - in an OpenSearch Response, can be used both to echo back the
	 * original query and to suggest new searches. Please see the OpenSearch
	 * Query specification for more information.
	 * 
	 * Note: New in version 1.1. Requirements: May appear zero or more times.
	 * Note that the "Q" is capitalized.
	 */

	public QueryList getQueries();
	
	public void setQueries(QueryList queries);

	public ResultList getResults();
	
	public void setResults(ResultList results);

}