package net.lucenews.model.exception;

import javax.servlet.http.HttpServletResponse;

public class IllegalActionException extends LuceneException {

	private static final long serialVersionUID = -5559876978766137177L;

	public IllegalActionException(String message) {
		super(message);
	}

	public int getStatus() {
		return HttpServletResponse.SC_FORBIDDEN;
	}

}
