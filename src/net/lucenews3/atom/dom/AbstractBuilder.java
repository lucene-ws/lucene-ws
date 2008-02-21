package net.lucenews3.atom.dom;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public abstract class AbstractBuilder {
	
	public void appendPropertyElement(Element parent, String name, Object value) {
		final Document document = parent.getOwnerDocument();
		final Element child = document.createElement(name);
		if (value != null) {
			child.appendChild(document.createTextNode(String.valueOf(value)));
		}
		parent.appendChild(child);
		
	}
	
}
