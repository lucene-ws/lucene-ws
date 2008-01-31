package net.lucenews.model.exception;

import javax.servlet.http.HttpServletResponse;

public class LuceneParseException extends LuceneException {

	private static final long serialVersionUID = 5253467267950571481L;

	public LuceneParseException(String message) {
		super(message);
	}

	public int getStatus() {
		return HttpServletResponse.SC_BAD_REQUEST;
	}

}
