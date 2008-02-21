package net.lucenews3.opensearch.atom;

import java.util.Calendar;

import net.lucenews3.atom.AuthorList;
import net.lucenews3.atom.CategoryList;
import net.lucenews3.atom.ContributorList;
import net.lucenews3.atom.EntryList;
import net.lucenews3.atom.Feed;
import net.lucenews3.atom.Generator;
import net.lucenews3.atom.LinkList;
import net.lucenews3.opensearch.Response;
import net.lucenews3.opensearch.ResponseImpl;

public class AtomResponse extends ResponseImpl implements Response, Feed {

	private String base;
	private String lang;
	private AuthorList authors;
	private CategoryList categories;
	private ContributorList contributors;
	private Generator generator;
	private String icon;
	private String id;
	private net.lucenews3.opensearch.LinkList links;
	private String logo;
	private String rights;
	private String subtitle;
	private String title;
	private Calendar updated;
	private EntryList entries;

	public String getBase() {
		return base;
	}

	public void setBase(String base) {
		this.base = base;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
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

	public net.lucenews3.opensearch.LinkList getLinks() {
		return links;
	}

	public void setLinks(LinkList links) {
		//this.links = links;
		throw new UnsupportedOperationException();
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

	public EntryList getEntries() {
		return entries;
	}

	public void setEntries(EntryList entries) {
		this.entries = entries;
	}
	
}
