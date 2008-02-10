package net.lucenews3.opensearch;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

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

	private List<Url> urls;

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

	static public final class SyndicationRight {
		static public final String OPEN = "open";

		static public final String LIMITED = "limited";

		static public final String PRIVATE = "private";

		static public final String CLOSED = "closed";
	}

	public DescriptionImpl() {
		urls = new LinkedList<Url>();
		images = new LinkedList<Image>();
		queries = new QueryListImpl();
		languages = new LinkedList<String>();
		inputEncodings = new LinkedList<String>();
		outputEncodings = new LinkedList<String>();
	}

	/* (non-Javadoc)
	 * @see net.lucenews3.opensearch.OpenSearchDescription#getShortName()
	 */

	public String getShortName() {
		return shortName;
	}

	/* (non-Javadoc)
	 * @see net.lucenews3.opensearch.OpenSearchDescription#setShortName(java.lang.String)
	 */
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	/* (non-Javadoc)
	 * @see net.lucenews3.opensearch.OpenSearchDescription#getDescription()
	 */

	public String getDescription() {
		return description;
	}

	/* (non-Javadoc)
	 * @see net.lucenews3.opensearch.OpenSearchDescription#setDescription(java.lang.String)
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/* (non-Javadoc)
	 * @see net.lucenews3.opensearch.OpenSearchDescription#getUrls()
	 */

	public List<Url> getUrls() {
		return urls;
	}

	public void addUrl(Url url) {
		urls.add(url);
	}

	public boolean removeUrl(Url url) {
		return urls.remove(url);
	}

	/* (non-Javadoc)
	 * @see net.lucenews3.opensearch.OpenSearchDescription#getContact()
	 */

	public String getContact() {
		return contact;
	}

	/* (non-Javadoc)
	 * @see net.lucenews3.opensearch.OpenSearchDescription#setContact(java.lang.String)
	 */
	public void setContact(String contact) {
		this.contact = contact;
	}

	/* (non-Javadoc)
	 * @see net.lucenews3.opensearch.OpenSearchDescription#getTags()
	 */

	public String getTags() {
		return tags;
	}

	/* (non-Javadoc)
	 * @see net.lucenews3.opensearch.OpenSearchDescription#setTags(java.lang.String)
	 */
	public void setTags(String tags) {
		this.tags = tags;
	}

	/* (non-Javadoc)
	 * @see net.lucenews3.opensearch.OpenSearchDescription#getLongName()
	 */

	public String getLongName() {
		return longName;
	}

	/* (non-Javadoc)
	 * @see net.lucenews3.opensearch.OpenSearchDescription#setLongName(java.lang.String)
	 */
	public void setLongName(String longName) {
		this.longName = longName;
	}

	/* (non-Javadoc)
	 * @see net.lucenews3.opensearch.OpenSearchDescription#getImages()
	 */

	public List<Image> getImages() {
		return images;
	}

	public void addImage(Image image) {
		images.add(image);
	}

	public boolean removeImage(Image image) {
		return images.remove(image);
	}

	/* (non-Javadoc)
	 * @see net.lucenews3.opensearch.OpenSearchDescription#getQueries()
	 */

	public QueryList getQueries() {
		return queries;
	}

	/* (non-Javadoc)
	 * @see net.lucenews3.opensearch.OpenSearchDescription#getDeveloper()
	 */

	public String getDeveloper() {
		return developer;
	}

	/* (non-Javadoc)
	 * @see net.lucenews3.opensearch.OpenSearchDescription#setDeveloper(java.lang.String)
	 */
	public void setDeveloper(String developer) {
		this.developer = developer;
	}

	/* (non-Javadoc)
	 * @see net.lucenews3.opensearch.OpenSearchDescription#getAttribution()
	 */

	public String getAttribution() {
		return attribution;
	}

	/* (non-Javadoc)
	 * @see net.lucenews3.opensearch.OpenSearchDescription#setAttribution(java.lang.String)
	 */
	public void setAttribution(String attribution) {
		this.attribution = attribution;
	}

	/* (non-Javadoc)
	 * @see net.lucenews3.opensearch.OpenSearchDescription#getSyndicationRight()
	 */

	public String getSyndicationRight() {
		return syndicationRight;
	}

	/* (non-Javadoc)
	 * @see net.lucenews3.opensearch.OpenSearchDescription#setSyndicationRight(java.lang.String)
	 */
	public void setSyndicationRight(String syndicationRight) {
		this.syndicationRight = syndicationRight;
	}

	/* (non-Javadoc)
	 * @see net.lucenews3.opensearch.OpenSearchDescription#hasValidSyndicationRight()
	 */
	public boolean hasValidSyndicationRight() {
		if (getSyndicationRight() == null) {
			return true;
		}

		String[] valid_rights = new String[] { SyndicationRight.OPEN,
				SyndicationRight.LIMITED, SyndicationRight.PRIVATE,
				SyndicationRight.CLOSED };

		for (int i = 0; i < valid_rights.length; i++) {
			if (getSyndicationRight().toLowerCase().equals(valid_rights[i])) {
				return true;
			}
		}

		return false;
	}

	/* (non-Javadoc)
	 * @see net.lucenews3.opensearch.OpenSearchDescription#getAdultContent()
	 */

	public Boolean getAdultContent() {
		return adultContent;
	}

	/* (non-Javadoc)
	 * @see net.lucenews3.opensearch.OpenSearchDescription#setAdultContent(java.lang.Boolean)
	 */
	public void setAdultContent(Boolean adultContent) {
		this.adultContent = adultContent;
	}

	/* (non-Javadoc)
	 * @see net.lucenews3.opensearch.OpenSearchDescription#setAdultContent(java.lang.String)
	 */
	public void setAdultContent(String adultContent) {
		if (adultContent == null) {
			this.adultContent = null;
		} else {
			this.adultContent = asBoolean(adultContent);
		}
	}

	/* (non-Javadoc)
	 * @see net.lucenews3.opensearch.OpenSearchDescription#getLanguages()
	 */

	public List<String> getLanguages() {
		return languages;
	}

	public void addLanguage(String language) {
		languages.add(language);
	}

	public boolean removeLanguage(String language) {
		return languages.remove(language);
	}

	/* (non-Javadoc)
	 * @see net.lucenews3.opensearch.OpenSearchDescription#getOutputEncodings()
	 */

	public List<String> getOutputEncodings() {
		return outputEncodings;
	}

	public void addOutputEncoding(String output_encoding) {
		outputEncodings.add(output_encoding);
	}

	public boolean removeOutputEncoding(String output_encoding) {
		return outputEncodings.remove(output_encoding);
	}

	/* (non-Javadoc)
	 * @see net.lucenews3.opensearch.OpenSearchDescription#getInputEncodings()
	 */

	public List<String> getInputEncodings() {
		return inputEncodings;
	}

	public void addInputEncoding(String input_encoding) {
		inputEncodings.add(input_encoding);
	}

	public boolean removeInputEncoding(String input_encoding) {
		return inputEncodings.remove(input_encoding);
	}

	/**
	 * Parses a DOM Document into an OpenSearch Description.
	 */

	public static Description asOpenSearchDescription(
			Document document) {
		DescriptionImpl description = new DescriptionImpl();

		// ShortName
		description.setShortName(asValue(document, "ShortName"));

		// Description
		description.setDescription(asValue(document, "Description"));

		// Url
		NodeList urls = document.getElementsByTagName("Url");
		for (int i = 0; i < urls.getLength(); i++) {
			// TODO: description.addUrl(OpenSearchUrl.asOpenSearchUrl((Element) urls
			//		.item(i)));
		}

		// Contact
		description.setContact(asValue(document, "Contact"));

		// Tags
		description.setTags(asValue(document, "Tags"));

		// LongName
		description.setLongName(asValue(document, "LongName"));

		// Image
		NodeList images = document.getElementsByTagName("Image");
		for (int i = 0; i < images.getLength(); i++) {
			// TODO: description.addImage(OpenSearchImage
					//.asOpenSearchImage((Element) images.item(i)));
		}

		// Query
		NodeList queries = document.getElementsByTagName("Query");
		for (int i = 0; i < queries.getLength(); i++) {
			// TODO:
			//description.addQuery(OpenSearchQuery
			//		.asOpenSearchQuery((Element) queries.item(i)));
		}

		// Developer
		description.setDeveloper(asValue(document, "Developer"));

		// Attribution
		description.setAttribution(asValue(document, "Attribution"));

		// SyndicationRight
		description.setSyndicationRight(asValue(document, "SyndicationRight"));

		// AdultContent
		description.setAdultContent(asValue(document, "AdultContent"));

		// Language
		String[] languages = asValues(document, "Language");
		for (int i = 0; i < languages.length; i++) {
			description.addLanguage(languages[i]);
		}

		// OutputEncoding
		String[] outputEncodings = asValues(document, "OutputEncoding");
		for (int i = 0; i < outputEncodings.length; i++) {
			description.addOutputEncoding(outputEncodings[i]);
		}

		// InputEncoding
		String[] inputEncodings = asValues(document, "InputEncoding");
		for (int i = 0; i < inputEncodings.length; i++) {
			description.addInputEncoding(inputEncodings[i]);
		}

		return description;
	}

	/**
	 * Transforms the OpenSearch description into a DOM Document.
	 * 
	 * @throws OpenSearchException
	 * @throws ParserConfigurationException
	 */

	public Document asDocument() throws OpenSearchException,
			ParserConfigurationException {
		Document document = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder().newDocument();
		document.appendChild(asElement(document));
		return document;
	}

	/**
	 * Transforms the OpenSearch description into a DOM Document.
	 * 
	 * @param mode
	 * @throws OpenSearchException
	 * @throws ParserConfigurationException
	 */

	public Document asDocument(OpenSearch.Mode mode)
			throws OpenSearchException, ParserConfigurationException {
		Document document = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder().newDocument();
		document.appendChild(asElement(document, mode));
		return document;
	}

	/**
	 * Transforms the OpenSearch Description into a DOM Element.
	 * 
	 * @param document
	 *            the parent DOM Document
	 * @throws OpenSearchException
	 */

	public Element asElement(Document document) throws OpenSearchException {
		return asElement(document, OpenSearch.getDefaultMode());
	}

	/**
	 * Transforms the OpenSearch Description into a DOM Element.
	 * 
	 * @param document
	 *            the parent DOM Document
	 * @param mode
	 *            the mode with which we are transforming
	 * @throws OpenSearchException
	 */

	public Element asElement(Document document, OpenSearch.Mode mode)
			throws OpenSearchException {
		Element element = document.createElement("OpenSearchDescription");
		element.setAttribute("xmlns", "http://a9.com/-/spec/opensearch/1.1/");

		// ShortName
		if (getShortName() == null) {
			if (mode == OpenSearch.STRICT) {
				throw new OpenSearchException("No short name specified");
			}
			if (mode == OpenSearch.ADAPTIVE) {
				element.appendChild(asElement(document, "ShortName", ""));
			}
		} else if (getShortName().length() > 16) {
			if (mode == OpenSearch.STRICT) {
				throw new OpenSearchException(
						"Short name cannot exceed 16 characters");
			}
			if (mode == OpenSearch.ADAPTIVE) {
				element.appendChild(asElement(document, "ShortName",
						getShortName().substring(0, 16)));
			} else {
				element.appendChild(asElement(document, "ShortName",
						getShortName()));
			}
		} else {
			element
					.appendChild(asElement(document, "ShortName",
							getShortName()));
		}

		// Description
		if ((getDescription() == null || getDescription().length() > 1024)
				&& mode == OpenSearch.STRICT) {
			throw new OpenSearchException("No description specified");
		}
		element
				.appendChild(asElement(document, "Description",
						getDescription()));

		// Url
		if (urls.size() == 0 && mode == OpenSearch.STRICT) {
			throw new OpenSearchException("Url must appear one or more times");
		}
		Iterator<Url> urlsIterator = urls.iterator();
		while (urlsIterator.hasNext()) {
			// TODO: element.appendChild(urlsIterator.next().asElement(document,
			//		OpenSearch.ATOM, mode));
		}

		// Contact
		if (getContact() != null) {
			int maximum = 64;
			if (getContact().length() > maximum) {
				if (mode == OpenSearch.STRICT) {
					throw new OpenSearchException("Contact cannot exceed "
							+ maximum + " characters");
				}
				if (mode == OpenSearch.ADAPTIVE) {
					element.appendChild(asElement(document, "Contact",
							getContact().substring(0, maximum)));
				} else {
					element.appendChild(asElement(document, "Contact",
							getContact()));
				}
			} else {
				element
						.appendChild(asElement(document, "Contact",
								getContact()));
			}
		}

		// Tags
		if (getTags() != null) {
			int maximum = 256;
			if (getTags().length() > maximum) {
				if (mode == OpenSearch.STRICT) {
					throw new OpenSearchException("Tags cannot exceed "
							+ maximum + " characters");
				}
				if (mode == OpenSearch.ADAPTIVE) {
					element.appendChild(asElement(document, "Tags", getTags()
							.substring(0, maximum)));
				} else {
					element.appendChild(asElement(document, "Tags", getTags()));
				}
			} else {
				element.appendChild(asElement(document, "Tags", getTags()));
			}
		}

		// LongName
		if (getLongName() != null) {
			int maximum = 48;
			if (getLongName().length() > maximum) {
				if (mode == OpenSearch.STRICT) {
					throw new OpenSearchException("Long name cannot exceed "
							+ maximum + " characters");
				}
				if (mode == OpenSearch.ADAPTIVE) {
					element.appendChild(asElement(document, "LongName",
							getLongName().substring(0, maximum)));
				} else {
					element.appendChild(asElement(document, "LongName",
							getLongName()));
				}
			} else {
				element.appendChild(asElement(document, "LongName",
						getLongName()));
			}
		}

		// Image
		if (getImages() != null) {
			Iterator<Image> images = getImages().iterator();
			while (images.hasNext()) {
				//OpenSearchImage image = images.next();
				//element.appendChild(image.asElement(document, mode));
				// TODO
			}
		}

		// Developer
		if (getDeveloper() != null) {
			int maximum = 64;
			if (getDeveloper().length() > maximum) {
				if (mode == OpenSearch.STRICT) {
					throw new OpenSearchException("Developer cannot exceed "
							+ maximum + " characters");
				}
				if (mode == OpenSearch.ADAPTIVE) {
					element.appendChild(asElement(document, "Developer",
							getDeveloper().substring(0, maximum)));
				} else {
					element.appendChild(asElement(document, "Developer",
							getDeveloper()));
				}
			} else {
				element.appendChild(asElement(document, "Developer",
						getDeveloper()));
			}
		}

		// Attribution
		if (getAttribution() != null) {
			int maximum = 256;
			if (getAttribution().length() > maximum) {
				if (mode == OpenSearch.STRICT) {
					throw new OpenSearchException("Attribution cannot exceed "
							+ maximum + " characters");
				}
				if (mode == OpenSearch.ADAPTIVE) {
					element.appendChild(asElement(document, "Attribution",
							getAttribution().substring(0, maximum)));
				} else {
					element.appendChild(asElement(document, "Attribution",
							getAttribution()));
				}
			} else {
				element.appendChild(asElement(document, "Attribution",
						getAttribution()));
			}
		}

		// SyndicationRight
		if (getSyndicationRight() != null) {
			if (mode == OpenSearch.STRICT && !hasValidSyndicationRight()) {
				throw new OpenSearchException("Invalid Syndication Right: "
						+ getSyndicationRight());
			}
			element.appendChild(asElement(document, "SyndicationRight",
					getSyndicationRight()));
		}

		// AdultContent
		if (getAdultContent() != null) {
			element.appendChild(asElement(document, "AdultContent", String
					.valueOf(getAdultContent())));
		}

		// Language
		Iterator<String> languageIterator = getLanguages().iterator();
		while (languageIterator.hasNext()) {
			element.appendChild(asElement(document, "Language",
					languageIterator.next()));
		}

		// OutputEncoding
		Iterator<String> outputEncodingIterator = getOutputEncodings()
				.iterator();
		while (outputEncodingIterator.hasNext()) {
			element.appendChild(asElement(document, "OutputEncoding",
					outputEncodingIterator.next()));
		}

		// InputEncoding
		Iterator<String> inputEncodingIterator = getInputEncodings().iterator();
		while (inputEncodingIterator.hasNext()) {
			element.appendChild(asElement(document, "InputEncoding",
					inputEncodingIterator.next()));
		}

		return element;
	}

	protected Element asElement(Document document, String name, String value) {
		if (value == null) {
			return null;
		}

		Element element = document.createElement(name);
		element.appendChild(document.createTextNode(value));
		return element;
	}

	protected static String asValue(Element element) {
		return element.getChildNodes().item(0).getNodeValue();
	}

	protected static String asValue(Document parent, String name) {
		return asValue(parent.getDocumentElement(), name);
	}

	protected static String asValue(Element parent, String name) {
		String[] values = asValues(parent, name);
		if (values.length > 0) {
			return values[0];
		}
		return null;
	}

	protected static String[] asValues(Document parent, String name) {
		return asValues(parent.getDocumentElement(), name);
	}

	protected static String[] asValues(Element parent, String name) {
		List<String> values = new LinkedList<String>();

		NodeList nodes = parent.getElementsByTagName(name);
		for (int i = 0; i < nodes.getLength(); i++) {
			Element element = (Element) nodes.item(i);
			values.add(asValue(element));
		}

		return values.toArray(new String[] {});
	}

	/**
	 * Parses boolean values as per A9 OpenSearch specification:
	 * 
	 * "false", "FALSE", "0", "no", and "NO" will all be considered FALSE, all
	 * other strings will be considered TRUE
	 */

	public static boolean asBoolean(String value) {
		String[] negatives = new String[] { "false", "FALSE", "0", "no", "NO" };
		for (int i = 0; i < negatives.length; i++) {
			if (value.equals(negatives[i])) {
				return false;
			}
		}
		return true;
	}

	public List<Url> setUrls(List<Url> urls) {
		// TODO Auto-generated method stub
		return null;
	}

	public Boolean isAdultContent() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setImages(List<Image> images) {
		// TODO Auto-generated method stub
		
	}

	public void setInputEncodings(List<String> inputEncodings) {
		// TODO Auto-generated method stub
		
	}

	public void setLanguages(List<String> languages) {
		// TODO Auto-generated method stub
		
	}

	public void setOutputEncodings(List<String> outputEncodings) {
		// TODO Auto-generated method stub
		
	}

	public void setQueries(QueryList queries) {
		this.queries = queries;
	}

}
