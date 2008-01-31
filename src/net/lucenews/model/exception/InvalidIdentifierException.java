package net.lucenews.model.exception;

import javax.servlet.http.HttpServletResponse;

public class InvalidIdentifierException extends LuceneException {

	private static final long serialVersionUID = -8120731325347989621L;

	private String identifier;

	public InvalidIdentifierException(String identifier) {
		this.identifier = identifier;
	}

	public String getMessage() {
		return "Identifier '" + identifier + "' is invalid.";
	}

	public int getStatus() {
		return HttpServletResponse.SC_BAD_REQUEST;
	}

}
