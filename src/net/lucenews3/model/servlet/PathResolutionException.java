package net.lucenews3.model.servlet;

public class PathResolutionException extends RuntimeException {

	private static final long serialVersionUID = -1952923520071018903L;

	public PathResolutionException() {
		super();
	}

	public PathResolutionException(String message) {
		super(message);
	}

	public PathResolutionException(Throwable cause) {
		super(cause);
	}

	public PathResolutionException(String message, Throwable cause) {
		super(message, cause);
	}

}
