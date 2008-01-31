package net.lucenews.model.exception;

import javax.servlet.http.HttpServletResponse;

public class DocumentsAlreadyExistException extends LuceneException {

	private static final long serialVersionUID = -8298519204513425284L;

	private String[] identifiers;

	public DocumentsAlreadyExistException(String... identifiers) {
		this.identifiers = identifiers;
	}

	public String getMessage() {
		StringBuffer buffer = new StringBuffer();

		if (identifiers.length == 1) {
			buffer.append("Document '" + identifiers[0] + "' already exists.");
		} else {
			buffer.append("Documents");

			for (int i = 0; i < identifiers.length; i++) {
				if (i > 0) {
					if (i == identifiers.length - 1) {
						buffer.append(" and");
					} else {
						buffer.append(",");
					}
				}

				buffer.append(" '" + identifiers[i] + "'");
			}

			buffer.append(" already exist.");
		}

		return String.valueOf(buffer);
	}

	public int getStatus() {
		return HttpServletResponse.SC_CONFLICT;
	}

}
