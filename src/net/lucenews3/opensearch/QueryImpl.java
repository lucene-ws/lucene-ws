package net.lucenews3.opensearch;

/**
 * OpenSearch Query provides a way of referring to a specific set of search
 * results. It is simple, powerful and flexible, and has a wide variety of uses.
 * In combination with an OpenSearch Description file (another of the components
 * of OpenSearch), it can be used to generate a request for search results.
 * 
 * Source: http://opensearch.a9.com/spec/1.1/query/
 */

public class QueryImpl implements Query {

	private String role;
	private String title;
	private String osd;
	private Integer totalResults;
	private String searchTerms;
	private Integer count;
	private Integer startIndex;
	private Integer startPage;
	private String language;
	private String outputEncoding;
	private String inputEncoding;

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getOsd() {
		return osd;
	}

	public void setOsd(String osd) {
		this.osd = osd;
	}

	public Integer getTotalResults() {
		return totalResults;
	}

	public void setTotalResults(Integer totalResults) {
		this.totalResults = totalResults;
	}

	public String getSearchTerms() {
		return searchTerms;
	}

	public void setSearchTerms(String searchTerms) {
		this.searchTerms = searchTerms;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Integer getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(Integer startIndex) {
		this.startIndex = startIndex;
	}

	public Integer getStartPage() {
		return startPage;
	}

	public void setStartPage(Integer startPage) {
		this.startPage = startPage;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getOutputEncoding() {
		return outputEncoding;
	}

	public void setOutputEncoding(String outputEncoding) {
		this.outputEncoding = outputEncoding;
	}

	public String getInputEncoding() {
		return inputEncoding;
	}

	public void setInputEncoding(String inputEncoding) {
		this.inputEncoding = inputEncoding;
	}

	public Integer[] getBoundingIndices() {
		Integer[] bounds = new Integer[] { null, null };

		Integer totalResults = getTotalResults();
		Integer count = getCount();

		if (totalResults == null || totalResults == 0 || count == null
				|| count == 0) {
			return bounds;
		}

		Integer startIndex = getStartIndex();
		if (startIndex == null) {
			startIndex = 1; // default
		}

		Integer startPage = getStartPage();
		if (getStartIndex() != null || startPage == null) {
			startPage = 1; // default
		}

		Integer firstIndex = startIndex + (startPage - 1) * count;
		Integer lastIndex = startIndex + (startPage * count) - 1;

		if (firstIndex > totalResults || lastIndex < 1) {
			return bounds;
		}

		bounds[0] = Math.max(firstIndex, 1);
		bounds[1] = Math.min(lastIndex, totalResults);

		return bounds;
	}

	public Integer getFirstIndex() {
		return getBoundingIndices()[0];
	}

	public Integer getLastIndex() {
		return getBoundingIndices()[1];
	}

	public Integer[] getPagingBoundaries() {
		Integer[] boundaries = new Integer[2];

		Integer totalResults = getTotalResults();
		Integer count = getCount();

		if (totalResults == null || totalResults == 0 || count == null
				|| count == 0) {
			return boundaries;
		}

		Integer startIndex = getStartIndex();
		if (startIndex == null) {
			startIndex = 1; // default
		}

		Integer startPage = getStartPage();
		if (startPage == null) {
			startPage = 1; // default
		}

		if (totalResults < startIndex) {
			return boundaries;
		}

		boundaries[0] = 1;
		boundaries[1] = (int) Math
				.ceil((totalResults - startIndex + 1) / count);

		return boundaries;
	}

	public Integer getFirstPage() {
		return getPagingBoundaries()[0];
	}

	public Integer getPreviousPage() {
		Integer startPage = getStartPage();
		Integer firstPage = getFirstPage();

		if (startPage == null) {
			startPage = 1;
		}

		if (firstPage == null || startPage.equals(firstPage)) {
			return null;
		}

		return startPage - 1;
	}

	public Integer getNextPage() {
		Integer startPage = getStartPage();
		Integer lastPage = getLastPage();

		if (startPage == null) {
			startPage = 1;
		}

		if (lastPage == null || startPage.equals(lastPage)) {
			return null;
		}

		return startPage + 1;
	}

	public Integer getLastPage() {
		return getPagingBoundaries()[1];
	}
}