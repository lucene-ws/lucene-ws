package net.lucenews.atom;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class AtomCollection {

	private String title, href, accept;

	public AtomCollection(String title, String href, String accept) {
		setTitle(title);
		setHref(href);
		setAccept(accept);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getHref() {
		return getHREF();
	}

	public String getHREF() {
		return href;
	}

	public void setHref(String href) {
		setHREF(href);
	}

	public void setHREF(String href) {
		this.href = href;
	}

	public String getAccept() {
		return accept;
	}

	public void setAccept(String accept) {
		this.accept = accept;
	}

	public Element asElement(Document document) {
		Element collection = document.createElement("collection");
		collection.setAttribute("href", String.valueOf(getHref()));

		Element title = document.createElement("atom:title");
		title.appendChild(document.createTextNode(String.valueOf(getTitle())));
		collection.appendChild(title);

		Element accept = document.createElement("accept");
		accept
				.appendChild(document.createTextNode(String
						.valueOf(getAccept())));
		collection.appendChild(accept);

		return collection;
	}

}
