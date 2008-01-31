package net.lucenews.model.exception;

public class IndexAlreadyExistsException extends IndicesAlreadyExistException {

	private static final long serialVersionUID = -4256304789117803012L;

	private String indexName;

	public IndexAlreadyExistsException(String indexName) {
		super(new String[] { indexName });
		this.indexName = indexName;
	}

	public String[] getIndexNames() {
		return new String[] { getIndexName() };
	}

	public String getIndexName() {
		return this.indexName;
	}

	public int size() {
		return 1;
	}

	public String getMessage() {
		return "Index '" + getIndexName() + "' already exists";
	}

}
