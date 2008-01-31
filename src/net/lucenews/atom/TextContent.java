package net.lucenews.atom;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class TextContent extends Content {

	private Text text;

	public TextContent(Text text) {
		this.text = text;
	}

	public TextContent(String text) {
		this(new Text(text, "text"));
	}

	public TextContent(String text, String type) {
		this(new Text(text, type));
	}

	public String getType() {
		return text.getType();
	}

	public Text getText() {
		return text;
	}

	public Element asElement(Document document) {
		Element element = super.asElement(document);
		element.appendChild(document.createTextNode(getText().toString()));
		return element;
	}

	public Node[] asNodes(Document document) {
		Text text = getText();

		Element parent = document.createElement("content");
		if (getType() != null)
			parent.setAttribute("type", getType());

		if (text == null || text.toString() == null)
			return new Node[] { parent };

		parent.appendChild(document.createTextNode(text.toString()));

		return new Node[] { parent };
	}

	public Document asDocument() throws ParserConfigurationException,
			TransformerConfigurationException, TransformerException {
		if (!isType("xhtml"))
			return null;

		Document document = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder().newDocument();
		Transformer transformer = TransformerFactory.newInstance()
				.newTransformer();

		transformer.transform(new StreamSource(new StringReader(getText()
				.toString())), new DOMResult(document));

		return document;
	}

}
