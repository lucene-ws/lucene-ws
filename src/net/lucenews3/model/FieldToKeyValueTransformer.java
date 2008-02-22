package net.lucenews3.model;

import org.apache.lucene.document.Field;

import net.lucenews3.Transformer;
import net.lucenews3.http.KeyValueImpl;
import net.lucenews3.http.KeyValue;

public class FieldToKeyValueTransformer implements Transformer<Field, KeyValue<String, String>> {

	@Override
	public KeyValue<String, String> transform(Field field) {
		return new KeyValueImpl<String, String>(field.name(), field.stringValue());
	}

	@Override
	public void transform(Field input, KeyValue<String, String> output) {
		throw new UnsupportedOperationException();
	}

}
