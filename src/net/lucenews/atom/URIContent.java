package net.lucenews.atom;

import java.net.URI;
import java.net.URISyntaxException;

public class URIContent extends Content {

	private URI uri;

	public URIContent(URI uri) {
		this.uri = uri;
	}

	public URIContent(URI uri, String type) {
		this.uri = uri;
		setType(type);
	}

	public URIContent(String uri) {
		try {
			this.uri = new URI(uri);
		} catch (URISyntaxException e) {
		}
	}

	public URIContent(String uri, String type) {
		try {
			this.uri = new URI(uri);
		} catch (URISyntaxException e) {
		}
		setType(type);
	}

	public URI getURI() {
		return uri;
	}

}
