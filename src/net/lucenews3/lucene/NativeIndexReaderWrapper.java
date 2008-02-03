package net.lucenews3.lucene;

import java.io.IOException;

import net.lucenews3.ExceptionMapper;

public abstract class NativeIndexReaderWrapper implements IndexReader {

	private org.apache.lucene.index.IndexReader nativeIndexReader;
	private ExceptionMapper exceptionMapper;
	
	public org.apache.lucene.index.IndexReader asNative() {
		return nativeIndexReader;
	}

	public void close() {
		try {
			nativeIndexReader.close();
		} catch (IOException exception) {
			throw exceptionMapper.map(exception);
		}
	}

}
