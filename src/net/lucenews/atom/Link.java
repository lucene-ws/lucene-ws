package net.lucenews.atom;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Link {

	private String href;

	private String rel;

	private String type;

	private String hreflang;

	private String title;

	private Integer length;

	public Link() {
	}

	public Link(String href) {
		this(href, null);
	}

	public Link(String href, String rel) {
		setHref(href);
		setRel(rel);
	}

	public Link(String href, String rel, String type) {
		setHref(href);
		setRel(rel);
		setType(type);
	}

	public static Link Alternate(String href) {
		return new Link(href, "alternate");
	}

	public static Link Edit(String href) {
		return new Link(href, "edit");
	}

	public static Link Enclosure(String href) {
		return new Link(href, "enclosure");
	}

	public static Link Related(String href) {
		return new Link(href, "related");
	}

	public static Link Self(String href) {
		return new Link(href, "self");
	}

	public static Link Via(String href) {
		return new Link(href, "via");
	}

	public String getHREF() {
		return href;
	}

	public String getHref() {
		return href;
	}

	public void setHREF(String href) {
		this.href = href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getRel() {
		return rel;
	}

	public void setRel(String rel) {
		this.rel = rel;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getHREFLang() {
		return hreflang;
	}

	public String getHrefLang() {
		return hreflang;
	}

	public void setHREFLang(String hreflang) {
		this.hreflang = hreflang;
	}

	public void setHrefLang(String hreflang) {
		this.hreflang = hreflang;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public Link clone() {
		Link link = new Link();
		link.setHref(getHref());
		link.setHrefLang(getHrefLang());
		link.setRel(getRel());
		link.setType(getType());
		link.setTitle(getTitle());
		link.setLength(getLength());
		return link;
	}

	public Element asElement(Document document) {
		Element link = document.createElement("link");

		if (getHref() != null) {
			link.setAttribute("href", String.valueOf(getHref()));
		}

		if (getRel() != null) {
			link.setAttribute("rel", getRel());
		}

		if (getType() != null) {
			link.setAttribute("type", getType());
		}

		if (getHrefLang() != null) {
			link.setAttribute("hreflang", getHrefLang());
		}

		if (getTitle() != null) {
			link.setAttribute("title", getTitle());
		}

		if (getLength() != null) {
			link.setAttribute("length", String.valueOf(getLength()));
		}

		return link;
	}

}
