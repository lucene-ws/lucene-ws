package net.lucenews3.model;

import net.lucenews3.Transformer;
import net.lucenews3.atom.dom4j.AbstractBuilder;

import org.apache.lucene.document.Field;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class FieldListToXoxoTransformer extends AbstractBuilder implements Transformer<FieldList, Element> {

	@Override
	public Element transform(FieldList fields) {
		final Element element = DocumentHelper.createElement("dl");
		transform(fields, element);
		return element;
	}

	@Override
	public void transform(FieldList fields, Element element) {
		for (Field field : fields) {
			final Element dt = DocumentHelper.createElement("dt");
			dt.add(DocumentHelper.createText(field.name()));
			element.add(dt);
			
			final Element dd = DocumentHelper.createElement("dd");
			dd.add(DocumentHelper.createText(field.stringValue()));
			element.add(dd);
		}
	}

}
