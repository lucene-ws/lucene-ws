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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Calendar getUpdated() {
		return updated;
	}

	public void setUpdated(Calendar updated) {
		this.updated = updated;
	}

	public Integer getTotalResults() {
		return totalResults;
	}

	public void setTotalResults(Integer totalResults) {
		this.totalResults = totalResults;
	}

	public Integer getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(Integer startIndex) {
		this.startIndex = startIndex;
	}

	public Integer getItemsPerPage() {
		return itemsPerPage;
	}

	public void setItemsPerPage(Integer itemsPerPage) {
		this.itemsPerPage = itemsPerPage;
	}

	public Link getLink() {
		return link;
	}

	public void setLink(Link link) {
		this.link = link;
	}

	public LinkList getLinks() {
		return links;
	}

	public void setLinks(LinkList links) {
		this.links = links;
	}

	public QueryList getQueries() {
		return queries;
	}

	public void setQueries(QueryList queries) {
		this.queries = queries;
	}

	public ResultList getResults() {
		return results;
	}

	public void setResults(ResultList results) {
		this.results = results;
	}

}
