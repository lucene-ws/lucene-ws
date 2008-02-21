package net.lucenews3;

public class ExceptionTranslatorImpl implements ExceptionTranslator {

	@Override
	public RuntimeException translate(Throwable cause) {
		return new RuntimeException(cause);
	}

}
