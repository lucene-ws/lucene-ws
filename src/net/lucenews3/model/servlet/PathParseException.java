package net.lucenews3.model.servlet;

public class PathParseException extends RuntimeException {

	private static final long serialVersionUID = -4637976137723912280L;

	public PathParseException() {
		super();
	}

	public PathParseException(String message) {
		super(message);
	}

	public PathParseException(Throwable cause) {
		super(cause);
	}

	public PathParseException(String message, Throwable cause) {
		super(message, cause);
	}

}
