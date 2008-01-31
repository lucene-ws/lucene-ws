package net.lucenews.model.exception;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

public class DocumentsNotFoundException extends LuceneException {

	private static final long serialVersionUID = 4226353110936165691L;

	private String[] ids;

	public DocumentsNotFoundException(String... documentIDs) {
		ids = documentIDs;
	}

	public DocumentsNotFoundException(List<String> documentIDs) {
		ids = documentIDs.toArray(new String[] {});
	}

	public String[] getDocumentIDs() {
		return ids;
	}

	public String getMessage() {
		StringBuffer buffer = new StringBuffer();

		if (ids.length == 1) {
			buffer.append("Document '" + ids[0] + "' not found.");
		} else {
			buffer.append("Documents");

			for (int i = 0; i < ids.length; i++) {
				if (i > 0) {
					if (i == (ids.length - 1)) {
						buffer.append(" and");
					} else {
						buffer.append(",");
					}
				}

				buffer.append(" '" + ids[i] + "'");
			}

			buffer.append(" not found.");
		}

		return String.valueOf(buffer);
	}

	public int getStatus() {
		return HttpServletResponse.SC_NOT_FOUND;
	}

}
