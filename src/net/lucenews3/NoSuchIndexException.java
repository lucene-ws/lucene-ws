package net.lucenews3;

public class NoSuchIndexException extends LuceneException {

	private static final long serialVersionUID = -6377797192011561739L;

	public NoSuchIndexException() {
		super();
	}

	public NoSuchIndexException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoSuchIndexException(String message) {
		super(message);
	}

	public NoSuchIndexException(Throwable cause) {
		super(cause);
	}

}
