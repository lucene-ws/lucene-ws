package net.lucenews3.http;

import java.io.IOException;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.store.LockObtainFailedException;

public class DefaultExceptionWrapper implements ExceptionWrapper {

	public RuntimeException wrap(IOException io) {
		return wrap((Exception) io);
	}
	
	public RuntimeException wrap(CorruptIndexException corruptIndex) {
		return wrap((Exception) corruptIndex);
	}
	
	public RuntimeException wrap(LockObtainFailedException lockObtainFailed) {
		return wrap((Exception) lockObtainFailed);
	}
	
	public RuntimeException wrap(Exception cause) {
		return new RuntimeException(cause);
	}
	
}
