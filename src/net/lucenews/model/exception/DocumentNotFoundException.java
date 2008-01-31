package net.lucenews.model.exception;

public class DocumentNotFoundException extends DocumentsNotFoundException {

	private static final long serialVersionUID = 7731485145200866652L;

	public DocumentNotFoundException(String id) {
		super(id);
	}

	public String getDocumentID() {
		String[] i = getDocumentIDs();
		if (i.length > 0) {
			return i[0];
		}
		return null;
	}

}
