package net.lucenews3;

public interface JSONStreamWriter {

	public void writeStartObject();

	public void writeEndObject();

	public void writeName(String name);

	public void writeValue(String value);

	public void writeStartArray();

	public void writeEndArray();

}
