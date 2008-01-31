package net.lucenews.model.exception;

public class DocumentAlreadyExistsException extends
		DocumentsAlreadyExistException {

	private static final long serialVersionUID = -6885164913071831990L;

	public DocumentAlreadyExistsException(String identifier) {
		super(identifier);
	}

}
