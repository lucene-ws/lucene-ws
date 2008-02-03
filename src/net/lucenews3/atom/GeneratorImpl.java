package net.lucenews3.atom;

public class GeneratorImpl extends CommonImpl implements Generator {

	private String uri;
	private String version;

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
}
