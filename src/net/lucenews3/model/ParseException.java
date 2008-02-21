package net.lucenews3.model;

public class ParseException extends RuntimeException {

	private static final long serialVersionUID = -1051076455540182798L;

	public ParseException() {
		super();
	}

	public ParseException(String message, Throwable cause) {
		super(message, cause);
	}

	public ParseException(String message) {
		super(message);
	}

	public ParseException(Throwable cause) {
		super(cause);
	}

}
