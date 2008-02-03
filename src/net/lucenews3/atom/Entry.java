package net.lucenews3.atom;

import java.util.Calendar;

public interface Entry extends Common {

	/**
	 * atom:entry elements MUST contain one or more atom:author elements, unless
	 * the atom:entry contains an atom:source element that contains an
	 * atom:author element or, in an Atom Feed Document, the atom:feed element
	 * contains an atom:author element itself.
	 * 
	 * @return
	 */
	public AuthorList getAuthors();

	public void setAuthors(AuthorList authors);

	/**
	 * atom:entry elements MAY contain any number of atom:category elements.
	 * 
	 * @return
	 */
	public CategoryList getCategories();

	public void setCategories(CategoryList categories);

	/**
	 * atom:entry elements MUST NOT contain more than one atom:content element.
	 * 
	 * @return
	 */
	public Content getContent();

	public void setContent(Content content);

	/**
	 * atom:entry elements MAY contain any number of atom:contributor elements.
	 * 
	 * @return
	 */
	public ContributorList getContributors();

	public void setContributors(ContributorList contributors);

	/**
	 * atom:entry elements MUST contain exactly one atom:id element.
	 * 
	 * @return
	 */
	public String getId();

	public void setId(String id);

	/**
	 * atom:entry elements that contain no child atom:content element MUST
	 * contain at least one atom:link element with a rel attribute value of
	 * "alternate".
	 * 
	 * atom:entry elements MUST NOT contain more than one atom:link element with
	 * a rel attribute value of "alternate" that has the same combination of
	 * type and hreflang attribute values.
	 * 
	 * atom:entry elements MAY contain additional atom:link elements beyond
	 * those described above.
	 * 
	 * @return
	 */
	public LinkList getLinks();

	public void setLinks(LinkList links);

	/**
	 * atom:entry elements MUST NOT contain more than one atom:published
	 * element.
	 * 
	 * @return
	 */
	public Object getPublished();

	public void setPublished(Object published);

	/**
	 * atom:entry elements MUST NOT contain more than one atom:rights element.
	 * 
	 * @return
	 */
	public String getRights();

	public void setRights(String rights);

	/**
	 * atom:entry elements MUST NOT contain more than one atom:source element.
	 * 
	 * @return
	 */
	public String getSource();

	public void setSource(String source);

	/**
	 * atom:entry elements MUST contain an atom:summary element in either of the
	 * following cases:
	 * <ul>
	 * <li>the atom:entry contains an atom:content that has a "src" attribute
	 * (and is thus empty).</li>
	 * <li>the atom:entry contains content that is encoded in Base64; i.e., the
	 * "type" attribute of atom:content is a MIME media type [MIMEREG], but is
	 * not an XML media type [RFC3023], does not begin with "text/", and does
	 * not end with "/xml" or "+xml".</li>
	 * </ul>
	 * 
	 * atom:entry elements MUST NOT contain more than one atom:summary element.
	 * 
	 * @return
	 */
	public String getSummary();

	public void setSummary(String summary);

	public String getTitle();

	public void setTitle(String title);

	public Calendar getUpdated();

	public void setUpdated(Calendar updated);

	public Object getExtensions();

	public void setExtensions(Object extensions);

}
