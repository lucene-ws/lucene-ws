package net.lucenews3;

public interface ExceptionTranslator {

	public RuntimeException translate(Exception exception);
	
}
