package net.lucenews3.opensearch;

import java.util.List;

public interface Url {

	/**
	 * template - a value containing the URL that will undergo parameter
	 * substitution.
	 * 
	 * Note: New in version 1.1. Requirements: Must appear one time.
	 */

	public String getTemplate();

	public void setTemplate(String template);

	/**
	 * type - the MIME type of the search results.
	 * 
	 * Note: New in version 1.1. Restrictions: MIME types must conform to the
	 * values defined in the IANA MIME Media Type Registry. Requirements: Must
	 * appear one time.
	 */

	public String getType();

	public void setType(String type);

	/**
	 * indexOffset - Contains the index number of the first search result.
	 * 
	 * Restrictions: The value must be an integer. Default: "1" Requirements:
	 * This attribute is optional.
	 */

	public Integer getIndexOffset();

	public void setIndexOffset(Integer indexOffset);

	/**
	 * pageOffset - Contains the page number of the first set of search results.
	 * 
	 * Restrictions: The value must be an integer. Default: "1". Requirements:
	 * This attribute is optional.
	 */

	public Integer getPageOffset();

	public void setPageOffset(Integer pageOffset);

	/**
	 * method - a value indicating the HTTP request method.
	 * 
	 * Note: New in version 1.1. Restrictions: A case insensitive value of
	 * either "get" or "post". Default: "get" Requirements: May appear one time.
	 */

	public String getMethod();

	public void setMethod(String method);

	public String getEncodingType();

	public void setEncodingType(String encodingType);

	public List<OpenSearchParameter> getParameters();

	public void setNamespace(String namespace, String uri);

}