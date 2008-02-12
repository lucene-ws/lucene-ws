package net.lucenews3.opensearch;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * OpenSearch Query Syntax is a simple way of specifying HTTP queries for the
 * purpose of requesting search results. Search engines can publish a URL in
 * Query Syntax, which can then be used by a search client to make requests.
 * This is usually done in an OpenSearch Description file, another of the
 * components of OpenSearch.
 * 
 * Query Syntax essentially consists of a template, which contains one or more
 * search parameters. With a simple substitution grammar, the parameters are
 * replaced with actual values to form a request. Several search parameters are
 * defined here, others may be used in a fashion similar to XML namespaces.
 * 
 * When found in an OpenSearch Description, the Url element may appear more than
 * once, listed in order of priority according to the search provider. Clients
 * will also take into account the response format when selecting which to use.
 * 
 * Source: http://opensearch.a9.com/spec/1.1/querysyntax/
 */

public class UrlImpl implements Url {

	private String template;

	private String type;

	private String method;

	private String encodingType;

	private Integer indexOffset;

	private Integer pageOffset;

	private List<OpenSearchParameter> parameters;

	private Map<String, String> namespaces;

	public UrlImpl() {
		parameters = new LinkedList<OpenSearchParameter>();
		namespaces = new LinkedHashMap<String, String>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.lucenews3.opensearch.OpenSearchUrl#getTemplate()
	 */

	public String getTemplate() {
		return template;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.lucenews3.opensearch.OpenSearchUrl#setTemplate(java.lang.String)
	 */
	public void setTemplate(String template) {
		this.template = template;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.lucenews3.opensearch.OpenSearchUrl#getType()
	 */

	public String getType() {
		return type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.lucenews3.opensearch.OpenSearchUrl#setType(java.lang.String)
	 */
	public void setType(String type) {
		this.type = type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.lucenews3.opensearch.OpenSearchUrl#getIndexOffset()
	 */

	public Integer getIndexOffset() {
		return indexOffset;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.lucenews3.opensearch.OpenSearchUrl#setIndexOffset(java.lang.Integer)
	 */
	public void setIndexOffset(Integer indexOffset) {
		this.indexOffset = indexOffset;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.lucenews3.opensearch.OpenSearchUrl#getPageOffset()
	 */

	public Integer getPageOffset() {
		return pageOffset;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.lucenews3.opensearch.OpenSearchUrl#setPageOffset(java.lang.Integer)
	 */
	public void setPageOffset(Integer pageOffset) {
		this.pageOffset = pageOffset;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.lucenews3.opensearch.OpenSearchUrl#getMethod()
	 */

	public String getMethod() {
		return method;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.lucenews3.opensearch.OpenSearchUrl#setMethod(java.lang.String)
	 */
	public void setMethod(String method) {
		this.method = method;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.lucenews3.opensearch.OpenSearchUrl#getEncodingType()
	 */
	public String getEncodingType() {
		return encodingType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.lucenews3.opensearch.OpenSearchUrl#setEncodingType(java.lang.String)
	 */
	public void setEncodingType(String encodingType) {
		this.encodingType = encodingType;
	}

	/**
	 * Parameter - An empty node that is used to describe HTTP POST parameters
	 * to be passed along with a query of method="post".
	 * 
	 * Parent: Url Attributes: o name - the name of the HTTP POST parameter. +
	 * Requirements: Must appear one time. o value - the value of the HTTP POST
	 * parameter. Will undergo parameter substitution before being sent. +
	 * Requirements: Must appear one time. Note: New in version 1.1. Note: The
	 * Param element is ignored if the method of the parent <Url> is anything
	 * other than "post". Note: If the parameter substitution results in an
	 * empty string, the parameter may still be sent with the query.
	 * Requirements: May appear zero, one, or more times.
	 */

	public void addParameter(String name, String value) {
		addParameter(new OpenSearchParameter(name, value));
	}

	public void addParameter(OpenSearchParameter parameter) {
		parameters.add(parameter);
	}

	public boolean removeParameter(String name) {
		return removeParameter(new OpenSearchParameter(name, null));
	}

	public boolean removeParameter(OpenSearchParameter parameter) {
		return parameters.remove(parameter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.lucenews3.opensearch.OpenSearchUrl#getParameters()
	 */
	public List<OpenSearchParameter> getParameters() {
		return parameters;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.lucenews3.opensearch.OpenSearchUrl#setNamespace(java.lang.String,
	 *      java.lang.String)
	 */
	public void setNamespace(String namespace, String uri) {
		namespaces.put(namespace, uri);
	}

}
