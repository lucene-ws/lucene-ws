package net.lucenews3.exception;

public class NoSuchIndexException extends WebServiceException {

	private static final long serialVersionUID = 3136593687792069053L;
	
	private String indexName;
	
	public NoSuchIndexException() {
		super();
	}
	
	public NoSuchIndexException(String indexName) {
		super();
		this.indexName = indexName;
	}

	public String getIndexName() {
		return indexName;
	}

	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}

}
