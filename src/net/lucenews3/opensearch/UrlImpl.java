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

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getIndexOffset() {
		return indexOffset;
	}

	public void setIndexOffset(Integer indexOffset) {
		this.indexOffset = indexOffset;
	}

	public Integer getPageOffset() {
		return pageOffset;
	}

	public void setPageOffset(Integer pageOffset) {
		this.pageOffset = pageOffset;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getEncodingType() {
		return encodingType;
	}

	public void setEncodingType(String encodingType) {
		this.encodingType = encodingType;
	}

	public List<OpenSearchParameter> getParameters() {
		return parameters;
	}

	public void setNamespace(String namespace, String uri) {
		namespaces.put(namespace, uri);
	}

}
