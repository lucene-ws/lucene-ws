package net.lucenews3.opensearch;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

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

	public static Image asOpenSearchImage(Element element) {
		Image image = new ImageImpl();

		// String url = element.getChildNodes().item(0).getNodeValue();

		String width = element.getAttribute("width");
		if (width != null) {
			image.setWidth(Integer.valueOf(width));
		}

		String height = element.getAttribute("height");
		if (height != null) {
			image.setWidth(Integer.valueOf(height));
		}

		String type = element.getAttribute("type");
		if (type != null) {
			image.setType(type);
		}

		return image;
	}

	/**
	 * Transforms the OpenSearch image into a DOM Element.
	 */

	public Element asElement(Document document) throws OpenSearchException {
		return asElement(document, OpenSearch.getDefaultMode());
	}

	public Element asElement(Document document, OpenSearch.Mode mode)
			throws OpenSearchException {
		Element element = document.createElement("Image");

		if (getUrl() != null) {
			element.appendChild(document.createTextNode(getUrl()));
		}

		if (getHeight() != null) {
			element.setAttribute("height", getHeight().toString());
		}

		if (getWidth() != null) {
			element.setAttribute("width", getWidth().toString());
		}

		if (getType() != null) {
			element.setAttribute("type", getType().toString());
		}

		return element;
	}

}
