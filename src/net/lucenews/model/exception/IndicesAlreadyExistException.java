package net.lucenews.model.exception;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

public class IndicesAlreadyExistException extends LuceneException {

	private static final long serialVersionUID = -1209193793379533816L;

	private String[] indexNames;

	public IndicesAlreadyExistException(String indexName) {
		indexNames = indexName.split(",");
	}

	public IndicesAlreadyExistException(String[] indexNames) {
		this.indexNames = indexNames;
	}

	public IndicesAlreadyExistException(List<String> indexNames) {
		this.indexNames = indexNames.toArray(new String[0]);
	}

	public int size() {
		return indexNames.length;
	}

	public String getIndexName() {
		StringBuffer buffer = new StringBuffer();

		for (int i = 0; i < indexNames.length; i++) {
			if (i > 0) {
				buffer.append(", ");
			}
			buffer.append(indexNames[i]);
		}

		return String.valueOf(buffer);
	}

	public String[] getIndexNames() {
		return indexNames;
	}

	public String getMessage() {
		StringBuffer buffer = new StringBuffer();
		String[] names = getIndexNames();

		if (names.length == 1) {
			buffer.append("Index ");
		} else {
			buffer.append("Indices ");
		}

		for (int i = 0; i < names.length; i++) {
			if (i > 0) {
				if (i == names.length - 1) {
					buffer.append(" and ");
				} else {
					buffer.append(", ");
				}
				buffer.append("'" + names[i] + "'");
			}
		}

		buffer.append(" already ");

		if (names.length == 1) {
			buffer.append("exists");
		} else {
			buffer.append("exist");
		}

		buffer.append(".");

		return String.valueOf(buffer);
	}

	public int getStatus() {
		return HttpServletResponse.SC_CONFLICT;
	}
}
