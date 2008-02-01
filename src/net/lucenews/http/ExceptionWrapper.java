package net.lucenews.http;

public interface ExceptionWrapper {

	public RuntimeException wrap(Exception exception);
	
}
