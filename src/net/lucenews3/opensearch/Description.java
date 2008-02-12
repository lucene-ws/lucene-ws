package net.lucenews3.opensearch;

import java.util.List;

public interface Description {

	/**
	 * ShortName - A brief name that will appear in buttons, UI controls, etc.,
	 * that reference this search content provider.
	 * 
	 * Parent: OpenSearchDescription Restrictions: Must contain 16 or fewer
	 * characters (no HTML). Requirements: Must appear exactly once.
	 */

	public String getShortName();

	public void setShortName(String shortName);

	/**
	 * Description - A human readable text description of the search content
	 * provider.
	 * 
	 * Parent: OpenSearchDescription Restrictions: Must contain 1024 or fewer
	 * characters of text (no HTML). Requirements: Must appear exactly once.
	 */

	public String getDescription();

	public void setDescription(String description);

	/**
	 * Url - Please see the OpenSearch Query Syntax specification for
	 * documentation on the Url element, its attributes, and its contents.
	 * 
	 * Parent: OpenSearchDescription Note: Updated in version 1.1. Requirements:
	 * Must appear one or more times.
	 */

	public List<Url> getUrls();
	
	public void setUrls(List<Url> urls);

	/**
	 * Contact - An email address at which the developer can be reached.
	 * 
	 * Parent: OpenSearchDescription Note: Updated in version 1.1. Restrictions:
	 * Must contain 64 or fewer characters of text (no HTML). Requirements: May
	 * appear zero or one time.
	 */

	public String getContact();

	public void setContact(String contact);

	/**
	 * Tags - A space-delimited set of words that are used as keywords to
	 * identify and categorize this search content.
	 * 
	 * Parent: OpenSearchDescription Note: Updated in version 1.1. Restrictions:
	 * Total length of all tags, including spaces, must contain 256 or fewer
	 * characters of text (no HTML). Requirements: May appear zero or one time.
	 */

	public String getTags();

	public void setTags(String tags);

	/**
	 * LongName - The name by which this search content provider is referred to
	 * in hypertext links, etc.
	 * 
	 * Parent: OpenSearchDescription Restrictions: Must contain 48 or fewer
	 * characters of text (no HTML). Default: The same value as the ShortName
	 * element. Requirements: May appear zero or one time.
	 */

	public String getLongName();

	public void setLongName(String longName);

	/**
	 * Image - A URL that identifies the location of an image that can be used
	 * in association with this search content.
	 * 
	 * Parent: OpenSearchDescription Attributes: o height - height, in pixels,
	 * of this image. Optional. o width - width, in pixels, of this image.
	 * Optional. o type - the MIME-type of this image. Optional. Note: Valid
	 * types correspond to those that can be displayed using the HTML <img
	 * src=""> element. Note: If more than one image is available, clients will
	 * choose the most suitable for the given application, giving preference to
	 * those listed first. Note: 64x64 JPEG/PNGs and 16x16 ICO files are
	 * recommended. Note: Updated in version 1.1. Requirements: May appear zero,
	 * one, or more times.
	 */

	public List<Image> getImages();
	
	public void setImages(List<Image> images);

	/**
	 * Query - used to indicate an example search; please see the OpenSearch
	 * Query specification for more information.
	 * 
	 * Parent: OpenSearchDescription Restrictions: The role attribute must equal
	 * example. Requirements: May appear zero or more times. Example: <Query
	 * role="example" searchTerms="cat" />
	 */

	public QueryList getQueries();
	
	public void setQueries(QueryList queries);

	/**
	 * The developer or maintainer of the OpenSearch feed.
	 * 
	 * Parent: OpenSearchDescription Restrictions: Must contain 64 or fewer
	 * characters of text (no HTML). Note: The Developer is not always the same
	 * as the owner, author, or copyright holder of the source of the content
	 * itself. The developer is simply the person or entity that created the
	 * feed, though they must have permission of the appropriate copyright
	 * holders. Requirements: May appear zero or one time.
	 */

	public String getDeveloper();

	public void setDeveloper(String developer);

	/**
	 * A list of all content sources or platforms that should be credited.
	 * 
	 * Parent: OpenSearchDescription Restrictions: Must contain 256 or fewer
	 * characters of text (no HTML). Note: Please include any copyright symbols
	 * or descriptive text as desired. Requirements: May appear zero or one
	 * time.
	 */

	public String getAttribution();

	public void setAttribution(String attribution);

	/**
	 * The degree to which the search results provided by this search engine can
	 * be distributed.
	 * 
	 * Parent: OpenSearchDescription Values: This element must contain one of
	 * the following values (case insensitive): o open - search results can be
	 * published or re-published without restriction. This is the default. o
	 * limited - search results can be published on the client site, but not
	 * further republished. o private - search feed may be queried, but the
	 * results may not be displayed at the client site. o closed - search feed
	 * should not be queried, and will disable the column for searches. Note:
	 * The SyndicationRight must be either open or limited for the content to
	 * appear on a public search aggregation site. Default: "open" Requirements:
	 * May appear zero or one time.
	 */

	public String getSyndicationRight();

	public void setSyndicationRight(String syndicationRight);

	/**
	 * A boolean flag that must be set if the content provided is not suitable
	 * for minors
	 * 
	 * Parent: OpenSearchDescription Values: "false", "FALSE", "0", "no", and
	 * "NO" will all be considered FALSE, all other strings will be considered
	 * TRUE Default: "FALSE" Requirements: May appear zero or one time.
	 */

	public Boolean isAdultContent();

	public void setAdultContent(Boolean adultContent);

	/**
	 * Indicates that the server is capable of returning results in the
	 * specified language.
	 * 
	 * Parent: OpenSearchDescription Restrictions: Values conform those of the
	 * XML 1.0 Language Identification, as specified by RFC 3066. Note: Please
	 * refer to the language parameter in the Query Syntax specification for
	 * more information on how clients can request a particular language for the
	 * search results. Note: New in version 1.1. Default: "*". Indicates that
	 * all languages may appear in the results. Requirements: May appear zero,
	 * one, or more times.
	 */

	public List<String> getLanguages();
	
	public void setLanguages(List<String> languages);

	/**
	 * Indicates that the server is capable of returning results with the
	 * specified character encoding.
	 * 
	 * Parent: OpenSearchDescription Restrictions: Values conform those of the
	 * XML 1.0 Character Encodings, as specified by the IANA Character Set
	 * Assignments. Note: Please refer to the outputEncoding parameter in the
	 * Query Syntax specification for more information on how clients can
	 * request a particular character encoding for the response. Note: New in
	 * version 1.1. Default: "UTF-8". Requirements: May appear zero, one, or
	 * more times.
	 */

	public List<String> getOutputEncodings();
	
	public void setOutputEncodings(List<String> outputEncodings);

	/**
	 * Indicates that the server is capable of reading queries with the
	 * specified character encoding.
	 * 
	 * Parent: OpenSearchDescription Restrictions: Values conform those of the
	 * XML 1.0 Character Encodings, as specified by the IANA Character Set
	 * Assignments. Note: Please refer to the inputEncoding parameter in the
	 * Query Syntax specification for more information on how clients can
	 * indicate a particular character encoding in the request. Note: New in
	 * version 1.1. Default: "UTF-8". Requirements: May appear zero, one, or
	 * more times.
	 */

	public List<String> getInputEncodings();
	
	public void setInputEncodings(List<String> inputEncodings);

}