package net.lucenews.atom;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Author extends Person {

	public Author(String name) {
		super(name);
	}

	public Author(String name, String uri, String email) {
		super(name, uri, email);
	}

	protected String getTagName() {
		return "author";
	}

	public static Author parse(Element element) {
		NodeList nodes = element.getChildNodes();
		String name = null;
		String uri = null;
		String email = null;

		for (int i = 0; i < nodes.getLength(); i++) {
			if (nodes.item(i).getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}

			Element _element = (Element) nodes.item(i);

			if (_element.getTagName().equals("name")) {
				name = _element.getFirstChild().getNodeValue();
			}

			if (_element.getTagName().equals("uri")) {
				uri = _element.getFirstChild().getNodeValue();
			}

			if (_element.getTagName().equals("email")) {
				email = _element.getFirstChild().getNodeValue();
			}
		}

		return new Author(name, uri, email);
	}

}
