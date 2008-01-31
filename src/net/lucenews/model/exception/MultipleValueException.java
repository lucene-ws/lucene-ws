package net.lucenews.model.exception;

import javax.servlet.http.HttpServletResponse;

public class MultipleValueException extends RuntimeException {

	private static final long serialVersionUID = 3649869456203920780L;

	public MultipleValueException() {
		super();
	}

	public MultipleValueException(String message) {
		super(message);
	}

	public int getStatus() {
		return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
	}

}
