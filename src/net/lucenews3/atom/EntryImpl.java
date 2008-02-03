package net.lucenews3.atom;

import java.util.Calendar;

public class EntryImpl extends CommonImpl implements Entry {

	private AuthorList authors;
	private CategoryList categories;
	private Content content;
	private ContributorList contributors;
	private String id;
	private LinkList links;
	private Object published;
	private String rights;
	private String source;
	private String summary;
	private String title;
	private Calendar updated;
	private Object extensions;

	public AuthorList getAuthors() {
		return authors;
	}

	public void setAuthors(AuthorList authors) {
		this.authors = authors;
	}

	public CategoryList getCategories() {
		return categories;
	}

	public void setCategories(CategoryList categories) {
		this.categories = categories;
	}

	public Content getContent() {
		return content;
	}

	public void setContent(Content content) {
		this.content = content;
	}

	public ContributorList getContributors() {
		return contributors;
	}

	public void setContributors(ContributorList contributors) {
		this.contributors = contributors;
	}

	public Object getExtensions() {
		return extensions;
	}

	public void setExtensions(Object extensions) {
		this.extensions = extensions;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public LinkList getLinks() {
		return links;
	}

	public void setLinks(LinkList links) {
		this.links = links;
	}

	public Object getPublished() {
		return published;
	}

	public void setPublished(Object published) {
		this.published = published;
	}

	public String getRights() {
		return rights;
	}

	public void setRights(String rights) {
		this.rights = rights;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Calendar getUpdated() {
		return updated;
	}

	public void setUpdated(Calendar updated) {
		this.updated = updated;
	}
	
}
