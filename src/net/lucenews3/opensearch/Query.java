package net.lucenews3.opensearch;

public interface Query {

	public static enum Role {
		EXAMPLE,
		RELATED,
		REQUEST,
		CORRECTION,
		SUPERSET,
		SUBSET,
	}

	public String getRole();

	public void setRole(String role);

	public String getTitle();

	public void setTitle(String title);

	public String getOsd();

	public void setOsd(String osd);

	public Integer getTotalResults();

	public void setTotalResults(Integer total_results);

	public String getSearchTerms();

	public void setSearchTerms(String search_terms);

	public Integer getCount();

	public void setCount(Integer count);

	/**
	 * startIndex
	 * 
	 * Description: The offset of the first search result, starting with one.
	 * Restrictions: A positive integer. Default: "1"
	 */

	public Integer getStartIndex();

	public void setStartIndex(Integer start_index);

	/**
	 * startPage
	 * 
	 * Description: The offset of the each group of count search results,
	 * starting with one. Restrictions: A positive integer. Default: "1"
	 */

	public Integer getStartPage();

	public void setStartPage(Integer start_page);

	public String getLanguage();

	public void setLanguage(String language);

	public String getOutputEncoding();

	public void setOutputEncoding(String output_encoding);

	public String getInputEncoding();

	public void setInputEncoding(String input_encoding);

	public Integer[] getBoundingIndices();

	/**
	 * Returns the first index of desired results in light of the total number
	 * of results specified. If either "count" or "totalResults" has not been
	 * specified, a null value is returned.
	 */

	public Integer getFirstIndex();

	/**
	 * Returns the last index of desired results in light of the total number of
	 * results specified. If either "count" or "totalResults" has not been
	 * specified, a null value is returned.
	 */

	public Integer getLastIndex();

	public Integer[] getPagingBoundaries();

	public Integer getFirstPage();

	public Integer getPreviousPage();

	public Integer getNextPage();

	public Integer getLastPage();

	public void setNamespace(String namespace, String uri);

}