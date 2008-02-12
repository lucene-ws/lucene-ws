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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
