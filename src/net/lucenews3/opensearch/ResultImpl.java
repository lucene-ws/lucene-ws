package net.lucenews3.opensearch;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.lucenews.atom.Link;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ResultImpl implements Result {

	private String title;

	private String id;

	private Calendar updated;

	private Float score;

	private List<Link> links;

	private OpenSearchText content;

	private List<OpenSearchPerson> people;

	private OpenSearchText rights;

	private String summary;

	public ResultImpl() {
		people = new LinkedList<OpenSearchPerson>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.lucenews3.opensearch.OpenSearchResult#getTitle()
	 */
	public String getTitle() {
		return title;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.lucenews3.opensearch.OpenSearchResult#setTitle(java.lang.String)
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.lucenews3.opensearch.OpenSearchResult#getId()
	 */
	public String getId() {
		return id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.lucenews3.opensearch.OpenSearchResult#setId(java.lang.String)
	 */
	public void setId(String id) {
		this.id = id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.lucenews3.opensearch.OpenSearchResult#getSummary()
	 */
	public String getSummary() {
		return summary;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.lucenews3.opensearch.OpenSearchResult#setSummary(java.lang.String)
	 */
	public void setSummary(String summary) {
		this.summary = summary;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.lucenews3.opensearch.OpenSearchResult#getUpdated()
	 */
	public Calendar getUpdated() {
		return updated;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.lucenews3.opensearch.OpenSearchResult#setUpdated(java.util.Calendar)
	 */
	public void setUpdated(Calendar updated) {
		this.updated = updated;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.lucenews3.opensearch.OpenSearchResult#getScore()
	 */
	public Float getScore() {
		return score;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.lucenews3.opensearch.OpenSearchResult#setScore(java.lang.Float)
	 */
	public void setScore(Float score) {
		this.score = score;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.lucenews3.opensearch.OpenSearchResult#getRelevance()
	 */
	@Deprecated
	public Float getRelevance() {
		return getScore();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.lucenews3.opensearch.OpenSearchResult#setRelevance(java.lang.Float)
	 */
	@Deprecated
	public void setRelevance(Float relevance) {
		setScore(relevance);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.lucenews3.opensearch.OpenSearchResult#getPeople()
	 */
	public List<OpenSearchPerson> getPeople() {
		return people;
	}

	public void addPerson(OpenSearchPerson person) {
		people.add(person);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.lucenews3.opensearch.OpenSearchResult#removePerson(net.lucenews3.opensearch.OpenSearchPerson)
	 */
	public boolean removePerson(OpenSearchPerson person) {
		return people.remove(person);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.lucenews3.opensearch.OpenSearchResult#getLinks()
	 */
	public List<Link> getLinks() {
		return links;
	}

	public void addLink(Link link) {
		if (links == null) {
			links = new ArrayList<Link>();
		}
		links.add(link);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.lucenews3.opensearch.OpenSearchResult#getContent()
	 */
	public OpenSearchText getContent() {
		return content;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.lucenews3.opensearch.OpenSearchResult#setContent(net.lucenews3.opensearch.OpenSearchText)
	 */
	public void setContent(OpenSearchText content) {
		this.content = content;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.lucenews3.opensearch.OpenSearchResult#setContent(java.lang.String)
	 */
	public void setContent(String content) {
		setContent(new OpenSearchText(content));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.lucenews3.opensearch.OpenSearchResult#setContent(org.w3c.dom.Node)
	 */
	public void setContent(Node content) {
		setContent(new OpenSearchText(content));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.lucenews3.opensearch.OpenSearchResult#setContent(org.w3c.dom.NodeList)
	 */
	public void setContent(NodeList content) {
		setContent(new OpenSearchText(content));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.lucenews3.opensearch.OpenSearchResult#getRights()
	 */
	public OpenSearchText getRights() {
		return rights;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.lucenews3.opensearch.OpenSearchResult#setRights(net.lucenews3.opensearch.OpenSearchText)
	 */
	public void setRights(OpenSearchText rights) {
		this.rights = rights;
	}

	public Element asElement(Document document, OpenSearch.Format format)
			throws OpenSearchException {
		return asElement(document, format, OpenSearch.STRICT);
	}

	public Element asElement(Document document, OpenSearch.Format format,
			OpenSearch.Mode mode) throws OpenSearchException {

		/**
		 * Atom
		 */

		if (format == OpenSearch.ATOM) {
			Element element = document.createElement("entry");

			// title
			if (getTitle() != null) {
				element.appendChild(asElement(document, "title", getTitle()));
			}

			// updated
			if (getUpdated() != null) {
				element.appendChild(asElement(document, "updated",
						net.lucenews.atom.Entry.asString(getUpdated())));
			}

			// link
			if (getLinks() != null) {
				for (Link link : links) {
					element.appendChild(link.asElement(document));
				}

			}

			// id
			if (getId() != null) {
				element.appendChild(asElement(document, "id", getId()));
			}

			// people
			Iterator<OpenSearchPerson> peopleIterator = getPeople().iterator();
			while (peopleIterator.hasNext()) {
				OpenSearchPerson person = peopleIterator.next();
				element.appendChild(person.asElement(document, format, mode));
			}

			// score
			if (getScore() != null) {
				element.appendChild(asElementNS(document,
						"http://a9.com/-/opensearch/extensions/relevance/1.0/",
						"relevance:score", getScore()));
			}

			// rights
			if (getRights() != null) {
				element.appendChild(getRights().asElement(document, format,
						mode));
			}

			// content
			if (getContent() != null) {
				element.appendChild(getContent().asElement(document, format,
						mode));
			}

			// summary
			if (getSummary() != null) {
				element
						.appendChild(asElement(document, "summary",
								getSummary()));
			}

			return element;
		}

		/**
		 * RSS
		 */

		if (format == OpenSearch.RSS) {
			Element element = document.createElement("item");

			// title
			if (getTitle() != null) {
				element.appendChild(asElement(document, "title", getTitle()));
			}

			// relevance
			if (getRelevance() != null) {
				element.appendChild(asElementNS(document,
						"http://a9.com/-/opensearch/extensions/relevance/1.0/",
						"relevance:score", getScore()));

				// backwards compatibility
				element.appendChild(asElementNS(document,
						"http://a9.com/-/spec/opensearch/1.1/",
						"opensearch:relevance", getScore()));
			}

			// guid
			if (getId() != null) {
				element.appendChild(asElement(document, "guid", getId()));
			}

			// updated
			if (getUpdated() != null) {
				String updated = net.lucenews.rss.Item.asString(getUpdated());
				element.appendChild(asElement(document, "lastBuildDate",
						updated));
			}

			// content
			if (getContent() != null) {
				element.appendChild(getContent().asElement(document, format,
						mode));
			}

			return element;
		}

		throw new OpenSearchException("Unknown format");
	}

	protected Element asElement(Document document, String name, Object value)
			throws OpenSearchException {
		Element element = document.createElement(name);
		element.appendChild(document.createTextNode(value.toString()));
		return element;
	}

	protected Element asElementNS(Document document, String namespaceURI,
			String qualifiedName, Object value) throws OpenSearchException {
		Element element = document.createElementNS(namespaceURI, qualifiedName);
		element.appendChild(document.createTextNode(value.toString()));
		return element;
	}

}
