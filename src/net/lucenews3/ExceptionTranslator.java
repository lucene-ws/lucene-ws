package net.lucenews3;

public interface ExceptionTranslator {

	public RuntimeException translate(Throwable cause);
	
	public Throwable unwrap(Throwable cause);
	
}
