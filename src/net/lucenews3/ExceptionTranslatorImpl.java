package net.lucenews3;

public class ExceptionTranslatorImpl implements ExceptionTranslator {

	@Override
	public RuntimeException translate(Exception exception) {
		return new RuntimeException(exception);
	}

}
