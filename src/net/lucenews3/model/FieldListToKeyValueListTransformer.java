package net.lucenews3.model;

import java.util.List;

import org.apache.lucene.document.Field;

import net.lucenews3.KeyValue;
import net.lucenews3.KeyValueList;
import net.lucenews3.KeyValueListImpl;
import net.lucenews3.Transformer;

public class FieldListToKeyValueListTransformer implements
		Transformer<List<Field>, KeyValueList<String, String>> {

	private Transformer<Field, KeyValue<String, String>> fieldTransformer;

	public FieldListToKeyValueListTransformer() {
		this.fieldTransformer = new FieldToKeyValueTransformer();
	}

	public FieldListToKeyValueListTransformer(
			Transformer<Field, KeyValue<String, String>> fieldTransformer) {
		this.fieldTransformer = fieldTransformer;
	}

	@Override
	public KeyValueList<String, String> transform(List<Field> input) {
		final KeyValueList<String, String> output = new KeyValueListImpl<String, String>();
		transform(input, output);
		return output;
	}

	@Override
	public void transform(List<Field> input, KeyValueList<String, String> output) {
		for (Field field : input) {
			output.add(fieldTransformer.transform(field));
		}
	}

	public Transformer<Field, KeyValue<String, String>> getFieldTransformer() {
		return fieldTransformer;
	}

	public void setFieldTransformer(
			Transformer<Field, KeyValue<String, String>> fieldTransformer) {
		this.fieldTransformer = fieldTransformer;
	}

}
