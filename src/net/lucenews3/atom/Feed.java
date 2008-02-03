package net.lucenews3.atom;

import java.util.Calendar;

public interface Feed extends Common {

	/**
	 * atom:feed elements MUST contain one or more atom:author elements, unless
	 * all of the atom:feed element's child atom:entry elements contain at least
	 * one atom:author element.
	 * 
	 * @return
	 */
	public AuthorList getAuthors();

	public void setAuthors(AuthorList authors);

	/**
	 * atom:feed elements MAY contain any number of atom:category elements.
	 * 
	 * @return
	 */
	public CategoryList getCategories();

	public void setCategories(CategoryList categories);

	/**
	 * atom:feed elements MAY contain any number of atom:contributor elements.
	 * 
	 * @return
	 */
	public ContributorList getContributors();

	public void setContributors(ContributorList contributors);

	/**
	 * atom:feed elements MUST NOT contain more than one atom:generator element.
	 * 
	 * @return
	 */
	public Generator getGenerator();

	public void setGenerator(Generator generator);

	/**
	 * atom:feed elements MUST NOT contain more than one atom:icon element.
	 * 
	 * @return
	 */
	public String getIcon();

	public void setIcon(String icon);

	/**
	 * atom:feed elements MUST contain exactly one atom:id element.
	 * 
	 * @return
	 */
	public String getId();

	public void setId(String id);

	/**
	 * atom:feed elements SHOULD contain one atom:link element with a rel
	 * attribute value of "self". This is the preferred URI for retrieving Atom
	 * Feed Documents representing this Atom feed.
	 * 
	 * atom:feed elements MUST NOT contain more than one atom:link element with
	 * a rel attribute value of "alternate" that has the same combination of
	 * type and hreflang attribute values.
	 * 
	 * atom:feed elements MAY contain additional atom:link elements beyond those
	 * described above.
	 * 
	 * @return
	 */
	public LinkList getLinks();

	public void setLinks(LinkList links);

	/**
	 * atom:feed elements MUST NOT contain more than one atom:logo element.
	 * 
	 * @return
	 */
	public String getLogo();

	public void setLogo(String logo);

	/**
	 * atom:feed elements MUST NOT contain more than one atom:rights element.
	 * 
	 * @return
	 */
	public String getRights();

	public void setRights(String rights);

	/**
	 * atom:feed elements MUST NOT contain more than one atom:subtitle element.
	 * 
	 * @return
	 */
	public String getSubtitle();

	public void setSubtitle(String subtitle);

	/**
	 * atom:feed elements MUST contain exactly one atom:title element.
	 * 
	 * @return
	 */
	public String getTitle();

	public void setTitle(String title);

	/**
	 * atom:feed elements MUST contain exactly one atom:updated element.
	 * 
	 * @return
	 */
	public Calendar getUpdated();

	public void setUpdated(Calendar updated);

	/**
	 * If multiple atom:entry elements with the same atom:id value appear in an
	 * Atom Feed Document, they represent the same entry. Their atom:updated
	 * timestamps SHOULD be different. If an Atom Feed Document contains
	 * multiple entries with the same atom:id, Atom Processors MAY choose to
	 * display all of them or some subset of them. One typical behavior would be
	 * to display only the entry with the latest atom:updated timestamp.
	 * 
	 * @return
	 */
	public EntryList getEntries();

	public void setEntries(EntryList entries);

}
