package net.lucenews3.opensearch;

public class OpenSearchException extends RuntimeException {

	private static final long serialVersionUID = 4087231066698720826L;

	public OpenSearchException() {
		super();
	}
	
	public OpenSearchException(String message) {
		super(message);
	}

	public OpenSearchException(String message, Throwable cause) {
		super(message, cause);
	}

	public OpenSearchException(Throwable cause) {
		super(cause);
	}

}
