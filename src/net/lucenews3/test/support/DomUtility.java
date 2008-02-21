package net.lucenews3.test.support;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DomUtility {

	private XPath xpath;
	
	public DomUtility() {
		XPathFactory factory = XPathFactory.newInstance();
		xpath = factory.newXPath();
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> toList(final List<?> list, Class<T> elementType) {
		final List<T> result = new ArrayList<T>(list.size());
		for (Object element : list) {
			result.add((T) element);
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> toList(final NodeList nodeList, Class<T> nodeType) {
		final int length = nodeList.getLength();
		final List<T> result = new ArrayList<T>(length);
		for (int i = 0; i < length; i++) {
			final Node node = nodeList.item(i);
			result.add((T) node);
		}
		return result;
	}
	
	public List<Node> toList(final NodeList nodeList) {
		return toList(nodeList, Node.class);
	}
	
	public List<Element> toElements(final NodeList nodeList) {
		return toList(nodeList, Element.class);
	}
	
	public List<Node> toList(Node[] nodes) {
		return Arrays.asList(nodes);
	}
	
	public List<Node> nodesByPath(final Object item, final String xpath) {
		try {
			XPathExpression expression = this.xpath.compile(xpath);
			return toList((NodeList) expression.evaluate(item, XPathConstants.NODESET));
		} catch (XPathExpressionException e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<Element> elementsByPath(final Object item, final String xpath) {
		return toList(nodesByPath(item, xpath), Element.class);
	}
	
	public Element elementByPath(final Object item, final String xpath) {
		List<Element> elements = elementsByPath(item, xpath);
		if (elements.isEmpty()) {
			return null;
		} else if (elements.size() == 1) {
			return elements.get(0);
		} else {
			throw new RuntimeException("Multiple elements at \"" + xpath + "\"");
		}
	}
	
	public String innerText(Node node) {
		final StringBuffer buffer = new StringBuffer();
		final NodeList childNodes = node.getChildNodes();
		final int length = childNodes.getLength();
		for (int i = 0; i < length; i++) {
			final Node childNode = childNodes.item(i);
			buffer.append(toString(childNode));
		}
		return buffer.toString();
	}
	
	public String toString(Node node) {
		return node.getNodeValue();
	}
	
}
