package net.lucenews3.model;

import net.lucenews3.Transformer;
import net.lucenews3.atom.dom4j.AbstractBuilder;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;

public class FieldListToXoxoTransformer extends AbstractBuilder implements Transformer<FieldList, Element> {

	private Namespace xhtmlNamespace;

	public FieldListToXoxoTransformer() {
		this.xhtmlNamespace = Namespace.get("xhtml", "http://www.w3.org/1999/xhtml");
	}
	
	@Override
	public Element transform(FieldList fields) {
		final Element element = DocumentHelper.createElement(QName.get("dl", xhtmlNamespace));
		element.addAttribute("class", "xoxo");
		transform(fields, element);
		return element;
	}

	@Override
	public void transform(FieldList fields, Element element) {
		for (Field field : fields) {
			String className = "";
			if (field.isIndexed()) {
				className += " indexed";
			}
			if (field.isStored()) {
				className += " stored";
			}
			if (field.isTokenized()) {
				className += " tokenized";
			}
			className = className.trim();
			
			final Element dt = DocumentHelper.createElement(QName.get("dt", xhtmlNamespace));
			dt.addAttribute("class", className);
			dt.add(DocumentHelper.createText(field.name()));
			element.add(dt);
			
			final Element dd = DocumentHelper.createElement(QName.get("dd", xhtmlNamespace));
			dd.add(DocumentHelper.createText(field.stringValue()));
			element.add(dd);
		}
	}

}
