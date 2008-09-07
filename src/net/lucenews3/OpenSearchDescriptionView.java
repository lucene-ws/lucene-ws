package net.lucenews3;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLStreamWriter;

import net.lucenews3.view.XMLStreamView;

public class OpenSearchDescriptionView extends XMLStreamView {

	public static final String DEFAULT_SEARCH_TERMS_PARAMETER_NAME = "searchTerms";
	public static final String DEFAULT_COUNT_PARAMETER_NAME        = "count";
	public static final String DEFAULT_START_INDEX_PARAMETER_NAME  = "startIndex";
	public static final String DEFAULT_START_PAGE_PARAMETER_NAME   = "startPage";

	private String openSearchNamespaceURI = "http://a9.com/-/spec/opensearch/1.1/";
	private String  searchTermsParameterName;
	private boolean searchTermsRequired;
	private String  countParameterName;
	private boolean countParameterRequired;
	private String  startIndexParameterName;
	private boolean startIndexRequired;
	private String  startPageParameterName;
	private boolean startPageRequired;

	public OpenSearchDescriptionView() {
		setContentType("application/opensearchdescription+xml;charset=utf-8");
		this.searchTermsParameterName = DEFAULT_SEARCH_TERMS_PARAMETER_NAME;
		this.countParameterName       = DEFAULT_COUNT_PARAMETER_NAME;
		this.startIndexParameterName  = DEFAULT_START_INDEX_PARAMETER_NAME;
		this.startPageParameterName   = DEFAULT_START_PAGE_PARAMETER_NAME;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void renderMergedOutputModel(Map model, HttpServletRequest request, HttpServletResponse response, XMLStreamWriter xml) throws Exception {
		Index index = (Index) model.get("index");
		String template = buildTemplate(request, index);
		
		xml.writeStartDocument();
		xml.writeStartElement("OpenSearchDescription");
		xml.writeDefaultNamespace(openSearchNamespaceURI);
		
		String shortName = index.getName();
		if (shortName == null) {
			xml.writeEmptyElement("ShortName");
		} else {
			xml.writeStartElement("ShortName");
			xml.writeCharacters(index.getName());
			xml.writeEndElement();
		}
		
		String description = index.getDisplayName();
		if (description == null) {
			xml.writeEmptyElement("Description");
		} else {
			xml.writeStartElement("Description");
			xml.writeCharacters(index.getDisplayName());
			xml.writeEndElement();
		}
		
		xml.writeStartElement("Url");
		xml.writeAttribute("template", template);
		xml.writeAttribute("type", "application/atom+xml");
		xml.writeEndElement();
		
		xml.writeEndElement(); // OpenSearchDescription
		xml.writeEndDocument();
	}

	protected String buildTemplate(HttpServletRequest request, Index index) throws URISyntaxException {
		Service service = getService();
		URI uri = service.getIndexURI(request, index);
		
		StringBuffer buffer = new StringBuffer();
		buffer.append(uri.toString());
		
		String delimiter = "?";
		
		if (searchTermsParameterName != null) {
			buffer.append(delimiter);
			appendTemplateParameter(buffer, "searchTerms", searchTermsParameterName, searchTermsRequired);
			delimiter = "&";
		}
		
		if (countParameterName != null) {
			buffer.append(delimiter);
			appendTemplateParameter(buffer, "count", countParameterName, countParameterRequired);
			delimiter = "&";
		}
		
		if (startIndexParameterName != null) {
			buffer.append(delimiter);
			appendTemplateParameter(buffer, "startIndex", startIndexParameterName, startIndexRequired);
			delimiter = "&";
		}
		
		if (startPageParameterName != null) {
			buffer.append(delimiter);
			appendTemplateParameter(buffer, "startPage", startPageParameterName, startPageRequired);
			delimiter = "&";
		}
		
		return buffer.toString();
	}

	public void appendTemplateParameter(StringBuffer template, String parameterName, String httpParameterName, boolean isRequired) {
		template.append(httpParameterName);
		template.append("=");
		template.append("{");
		template.append(parameterName);
		if (!isRequired) {
			template.append("?");
		}
		template.append("}");
	}

	public String getSearchTermsParameterName() {
		return searchTermsParameterName;
	}

	public void setSearchTermsParameterName(String searchTermsParameterName) {
		this.searchTermsParameterName = searchTermsParameterName;
	}

	public boolean isSearchTermsRequired() {
		return searchTermsRequired;
	}

	public void setSearchTermsRequired(boolean searchTermsRequired) {
		this.searchTermsRequired = searchTermsRequired;
	}

	public String getCountParameterName() {
		return countParameterName;
	}

	public void setCountParameterName(String countParameterName) {
		this.countParameterName = countParameterName;
	}

	public boolean isCountParameterRequired() {
		return countParameterRequired;
	}

	public void setCountParameterRequired(boolean countParameterRequired) {
		this.countParameterRequired = countParameterRequired;
	}

	public String getStartIndexParameterName() {
		return startIndexParameterName;
	}

	public void setStartIndexParameterName(String startIndexParameterName) {
		this.startIndexParameterName = startIndexParameterName;
	}

	public boolean isStartIndexRequired() {
		return startIndexRequired;
	}

	public void setStartIndexRequired(boolean startIndexRequired) {
		this.startIndexRequired = startIndexRequired;
	}

	public String getStartPageParameterName() {
		return startPageParameterName;
	}

	public void setStartPageParameterName(String startPageParameterName) {
		this.startPageParameterName = startPageParameterName;
	}

	public boolean isStartPageRequired() {
		return startPageRequired;
	}

	public void setStartPageRequired(boolean startPageRequired) {
		this.startPageRequired = startPageRequired;
	}

}
