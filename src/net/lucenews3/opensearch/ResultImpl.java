package net.lucenews3.opensearch;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class ResultImpl implements Result {

	private String title;
	private String id;
	private Calendar updated;
	private Double relevance;
	private LinkList links;
	private Content content;
	private List<OpenSearchPerson> people;
	private Content rights;
	private String summary;

	public ResultImpl() {
		people = new LinkedList<OpenSearchPerson>();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public Calendar getUpdated() {
		return updated;
	}

	public void setUpdated(Calendar updated) {
		this.updated = updated;
	}

	public Double getRelevance() {
		return relevance;
	}

	public void setRelevance(Double relevance) {
		this.relevance = relevance;
	}

	public List<OpenSearchPerson> getPeople() {
		return people;
	}

	public void addPerson(OpenSearchPerson person) {
		people.add(person);
	}

	public boolean removePerson(OpenSearchPerson person) {
		return people.remove(person);
	}

	public LinkList getLinks() {
		return links;
	}
	
	public void setLinks(LinkList links) {
		this.links = links;
	}

	public Content getContent() {
		return content;
	}

	public void setContent(Content content) {
		this.content = content;
	}

	public Content getRights() {
		return rights;
	}

	public void setRights(Content rights) {
		this.rights = rights;
	}

}
