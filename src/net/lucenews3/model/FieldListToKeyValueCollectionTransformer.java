package net.lucenews3.model;

import java.util.List;

import org.apache.lucene.document.Field;

import net.lucenews3.KeyValue;
import net.lucenews3.KeyValueList;
import net.lucenews3.Transformer;

public class FieldListToKeyValueCollectionTransformer implements Transformer<List<Field>, KeyValueList<String, String>> {

	private Transformer<Field, KeyValue<String, String>> fieldTransformer;

	public FieldListToKeyValueCollectionTransformer() {
		this.fieldTransformer = new FieldToKeyValueTransformer();
	}
	
	public FieldListToKeyValueCollectionTransformer(Transformer<Field, KeyValue<String, String>> fieldTransformer) {
		this.fieldTransformer = fieldTransformer;
	}
	
	@Override
	public KeyValueList<String, String> transform(List<Field> input) {
		final KeyValueList<String, String> output = null; // TODO
		transform(input, output);
		return output;
	}

	@Override
	public void transform(List<Field> input,
			KeyValueList<String, String> output) {
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
