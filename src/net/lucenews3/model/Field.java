package net.lucenews3.model;

import org.apache.lucene.document.Fieldable;

public interface Field extends Fieldable, NativeImplementationProvider<org.apache.lucene.document.Field> {

	@Override
	public org.apache.lucene.document.Field asNative();
	
}
