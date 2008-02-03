package net.lucenews3.atom;

import java.util.Calendar;

public class FeedImpl extends CommonImpl implements Feed {

	private AuthorList authors;
	private CategoryList categories;
	private ContributorList contributors;
	private Generator generator;
	private String icon;
	private String id;
	private LinkList links;
	private String logo;
	private String rights;
	private String subtitle;
	private String title;
	private Calendar updated;
	private EntryList entries;

	public FeedImpl() {
		this.authors = new AuthorListImpl();
		this.categories = new CategoryListImpl();
		this.contributors = new ContributorListImpl();
		this.links = new LinkListImpl();
		this.entries = new EntryListImpl();
	}
	
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

	public ContributorList getContributors() {
		return contributors;
	}

	public void setContributors(ContributorList contributors) {
		this.contributors = contributors;
	}

	public EntryList getEntries() {
		return entries;
	}

	public void setEntries(EntryList entries) {
		this.entries = entries;
	}

	public Generator getGenerator() {
		return generator;
	}

	public void setGenerator(Generator generator) {
		this.generator = generator;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
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

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getRights() {
		return rights;
	}

	public void setRights(String rights) {
		this.rights = rights;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
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
