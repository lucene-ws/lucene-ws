package net.lucenews3;

public class NoSuchDocumentException extends LuceneException {

	private static final long serialVersionUID = -2491154475137486728L;

	public NoSuchDocumentException() {
		super();
	}

	public NoSuchDocumentException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoSuchDocumentException(String message) {
		super(message);
	}

	public NoSuchDocumentException(Throwable cause) {
		super(cause);
	}

}
