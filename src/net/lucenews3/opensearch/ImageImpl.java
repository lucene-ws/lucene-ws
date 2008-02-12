package net.lucenews3.opensearch;

public class ImageImpl implements Image {

	private String url;

	private Integer height;

	private Integer width;

	private String type;

	public ImageImpl() {
	}

	public ImageImpl(String url) {
		setUrl(url);
	}

	/* (non-Javadoc)
	 * @see net.lucenews3.opensearch.OpenSearchImage#getUrl()
	 */
	public String getUrl() {
		return url;
	}

	/* (non-Javadoc)
	 * @see net.lucenews3.opensearch.OpenSearchImage#setUrl(java.lang.String)
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/* (non-Javadoc)
	 * @see net.lucenews3.opensearch.OpenSearchImage#getHeight()
	 */
	public Integer getHeight() {
		return height;
	}

	/* (non-Javadoc)
	 * @see net.lucenews3.opensearch.OpenSearchImage#setHeight(java.lang.Integer)
	 */
	public void setHeight(Integer height) {
		this.height = height;
	}

	/* (non-Javadoc)
	 * @see net.lucenews3.opensearch.OpenSearchImage#getWidth()
	 */
	public Integer getWidth() {
		return width;
	}

	/* (non-Javadoc)
	 * @see net.lucenews3.opensearch.OpenSearchImage#setWidth(java.lang.Integer)
	 */
	public void setWidth(Integer width) {
		this.width = width;
	}

	/* (non-Javadoc)
	 * @see net.lucenews3.opensearch.OpenSearchImage#getType()
	 */
	public String getType() {
		return type;
	}

	/* (non-Javadoc)
	 * @see net.lucenews3.opensearch.OpenSearchImage#setType(java.lang.String)
	 */
	public void setType(String type) {
		this.type = type;
	}

}
