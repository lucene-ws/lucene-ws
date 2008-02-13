package net.lucenews3.opensearch;

import java.util.LinkedList;
import java.util.List;

/**
 * OpenSearch Description files are used by search engines to describe
 * themselves and how they can be queried (using OpenSearch Query Syntax). They
 * are published as simple XML files over the web, and can be used by search
 * clients to integrate third-party searches. Search results may be in the form
 * of OpenSearch Response, another of the components of OpenSearch.
 * 
 * Source: http://opensearch.a9.com/spec/1.1/description/
 * 
 */

public class DescriptionImpl implements Description {

	private String shortName;
	private String description;
	private UrlList urls;
	private String contact;
	private String tags;
	private String longName;
	private List<Image> images;
	private QueryList queries;
	private String developer;
	private String attribution;
	private String syndicationRight;
	private Boolean adultContent;
	private List<String> languages;
	private List<String> inputEncodings;
	private List<String> outputEncodings;

	public DescriptionImpl() {
		urls = new UrlListImpl();
		images = new LinkedList<Image>();
		queries = new QueryListImpl();
		languages = new LinkedList<String>();
		inputEncodings = new LinkedList<String>();
		outputEncodings = new LinkedList<String>();
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public UrlList getUrls() {
		return urls;
	}

	public void setUrls(UrlList urls) {
		this.urls = urls;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getLongName() {
		return longName;
	}

	public void setLongName(String longName) {
		this.longName = longName;
	}

	public List<Image> getImages() {
		return images;
	}

	public void setImages(List<Image> images) {
		this.images = images;
	}

	public QueryList getQueries() {
		return queries;
	}

	public void setQueries(QueryList queries) {
		this.queries = queries;
	}

	public String getDeveloper() {
		return developer;
	}

	public void setDeveloper(String developer) {
		this.developer = developer;
	}

	public String getAttribution() {
		return attribution;
	}

	public void setAttribution(String attribution) {
		this.attribution = attribution;
	}

	public String getSyndicationRight() {
		return syndicationRight;
	}

	public void setSyndicationRight(String syndicationRight) {
		this.syndicationRight = syndicationRight;
	}

	public Boolean isAdultContent() {
		return adultContent;
	}

	public void setAdultContent(Boolean adultContent) {
		this.adultContent = adultContent;
	}

	public List<String> getLanguages() {
		return languages;
	}

	public void setLanguages(List<String> languages) {
		this.languages = languages;
	}

	public List<String> getInputEncodings() {
		return inputEncodings;
	}

	public void setInputEncodings(List<String> inputEncodings) {
		this.inputEncodings = inputEncodings;
	}

	public List<String> getOutputEncodings() {
		return outputEncodings;
	}

	public void setOutputEncodings(List<String> outputEncodings) {
		this.outputEncodings = outputEncodings;
	}

}
