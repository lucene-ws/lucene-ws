package net.lucenews3;

public class LuceneException extends RuntimeException {

	private static final long serialVersionUID = 7990969141509636322L;

	public LuceneException() {
		super();
	}

	public LuceneException(String message, Throwable cause) {
		super(message, cause);
	}

	public LuceneException(String message) {
		super(message);
	}

	public LuceneException(Throwable cause) {
		super(cause);
	}

}
