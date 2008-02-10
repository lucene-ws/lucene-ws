package net.lucenews3.opensearch;

import java.util.Calendar;

import net.lucenews.atom.Link;

/**
 * OpenSearch Response elements are used to add search data to existing
 * XML-based syndication formats, such as RSS 2.0 and Atom 1.0. These extensions
 * are designed to work with OpenSearch Description files, another of the
 * components of OpenSearch, to provide rich search syndication with a minimal
 * amount of overhead.
 * 
 * OpenSearch Response introduces several elements that provide the necessary
 * information for syndicating search results. Additionally, OpenSearch Response
 * recommends clean extensibility guidelines, and suggests that clients and
 * servers recognize certain other common extensions to enhance search results.
 * 
 * Source: http://opensearch.a9.com/spec/1.1/response/
 * 
 */

public class ResponseImpl implements Response {

	private String title;

	private String id;

	private Calendar updated;

	private String description;

	private Integer totalResults;

	private Integer startIndex;

	private Integer itemsPerPage;

	private Link link;

	private LinkList links;

	private QueryList queries;

	private ResultList results;

	public ResponseImpl() {
		links = new LinkListImpl();
		queries = new QueryListImpl();
		results = new ResultListImpl();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.lucenews3.opensearch.OpenSearchResponse#getTitle()
	 */
	public String getTitle() {
		return title;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.lucenews3.opensearch.OpenSearchResponse#setTitle(java.lang.String)
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.lucenews3.opensearch.OpenSearchResponse#getDescription()
	 */
	public String getDescription() {
		return description;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.lucenews3.opensearch.OpenSearchResponse#setDescription(java.lang.String)
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.lucenews3.opensearch.OpenSearchResponse#getId()
	 */
	public String getId() {
		return id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.lucenews3.opensearch.OpenSearchResponse#setId(java.lang.String)
	 */
	public void setId(String id) {
		this.id = id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.lucenews3.opensearch.OpenSearchResponse#getUpdated()
	 */
	public Calendar getUpdated() {
		return updated;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.lucenews3.opensearch.OpenSearchResponse#setUpdated(java.util.Calendar)
	 */
	public void setUpdated(Calendar updated) {
		this.updated = updated;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.lucenews3.opensearch.OpenSearchResponse#getTotalResults()
	 */

	public Integer getTotalResults() {
		return totalResults;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.lucenews3.opensearch.OpenSearchResponse#setTotalResults(java.lang.Integer)
	 */
	public void setTotalResults(Integer totalResults) {
		this.totalResults = totalResults;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.lucenews3.opensearch.OpenSearchResponse#getStartIndex()
	 */

	public Integer getStartIndex() {
		return startIndex;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.lucenews3.opensearch.OpenSearchResponse#setStartIndex(java.lang.Integer)
	 */
	public void setStartIndex(Integer startIndex) {
		this.startIndex = startIndex;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.lucenews3.opensearch.OpenSearchResponse#getItemsPerPage()
	 */

	public Integer getItemsPerPage() {
		return itemsPerPage;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.lucenews3.opensearch.OpenSearchResponse#setItemsPerPage(java.lang.Integer)
	 */
	public void setItemsPerPage(Integer itemsPerPage) {
		this.itemsPerPage = itemsPerPage;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.lucenews3.opensearch.OpenSearchResponse#getLink()
	 */

	public Link getLink() {
		return link;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.lucenews3.opensearch.OpenSearchResponse#setLink(net.lucenews.atom.Link)
	 */
	public void setLink(Link link) {
		this.link = link;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.lucenews3.opensearch.OpenSearchResponse#getLinks()
	 */
	public LinkList getLinks() {
		return links;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.lucenews3.opensearch.OpenSearchResponse#getQueries()
	 */

	public QueryList getQueries() {
		return queries;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.lucenews3.opensearch.OpenSearchResponse#getResults()
	 */
	public ResultList getResults() {
		return results;
	}

	public void setLinks(LinkList links) {
		this.links = links;
	}

	public void setQueries(QueryList queries) {
		this.queries = queries;
	}

	public void setResults(ResultList results) {
		this.results = results;
	}

}
