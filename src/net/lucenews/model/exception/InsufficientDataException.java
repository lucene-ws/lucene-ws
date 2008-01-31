package net.lucenews.model.exception;

import javax.servlet.http.HttpServletResponse;

public class InsufficientDataException extends LuceneException {

	private static final long serialVersionUID = -3024406459602584471L;

	public InsufficientDataException(String message) {
		super(message);
	}

	public int getStatus() {
		return HttpServletResponse.SC_BAD_REQUEST;
	}

}
