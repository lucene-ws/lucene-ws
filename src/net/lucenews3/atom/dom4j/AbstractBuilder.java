package net.lucenews3.atom.dom4j;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.QName;

public abstract class AbstractBuilder {
	
	public void addProperty(Element element, String tagName, Object value) {
		addProperty(element, tagName, value, true);
	}
	
	public void addProperty(Element element, String tagName, Object value, boolean isRequired) {
		final Element child = DocumentHelper.createElement(tagName);
		if (value == null) {
			if (isRequired) {
				child.add(DocumentHelper.createText(""));
			}
		} else {
			child.add(DocumentHelper.createText(String.valueOf(value)));
		}
		element.add(child);
		
	}

	public void addProperty(Element element, QName qualifiedName, Object value) {
		addProperty(element, qualifiedName, value, true);
	}
	
	public void addProperty(Element element, QName qualifiedName, Object value, boolean isRequired) {
		final Element child = DocumentHelper.createElement(qualifiedName);
		if (value == null) {
			if (isRequired) {
				child.add(DocumentHelper.createText(""));
			}
		} else {
			child.add(DocumentHelper.createText(String.valueOf(value)));
		}
		element.add(child);
	}
	
	public void addPropertyNS(Element element, String uri, String qualifiedName, Object value) {
		addPropertyNS(element, uri, qualifiedName, value, true);
	}
	
	public void addPropertyNS(Element element, String uri, String qualifiedTagName, Object value, boolean isRequired) {
		final Element child = DocumentHelper.createElement(QName.get(qualifiedTagName, uri));
		if (value == null) {
			if (isRequired) {
				child.add(DocumentHelper.createText(""));
			}
		} else {
			child.add(DocumentHelper.createText(String.valueOf(value)));
		}
		element.add(child);
	}
	
	public void addAttribute(Element element, String name, Object value) {
		addAttribute(element, name, value, true);
	}
	
	public void addAttribute(Element element, String name, Object value, boolean isRequired) {
		if (value == null) {
			if (isRequired) {
				element.addAttribute(name, "");
			}
		} else {
			element.addAttribute(name, String.valueOf(value));
		}
	}
	
	public void addAttributeNS(Element element, String uri, String qualifiedName, Object value) {
		addAttributeNS(element, uri, qualifiedName, value, true);
	}
	
	public void addAttributeNS(Element element, String uri, String qualifiedName, Object value, boolean isRequired) {
		if (value == null) {
			if (isRequired) {
				element.addAttribute(QName.get(qualifiedName, uri), "");
			}
		} else {
			element.addAttribute(QName.get(qualifiedName, uri), String.valueOf(value));
		}
	}
	
}
