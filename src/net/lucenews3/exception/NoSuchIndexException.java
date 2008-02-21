package net.lucenews3.exception;

import net.lucenews3.model.IndexIdentity;

public class NoSuchIndexException extends WebServiceException {

	private static final long serialVersionUID = 3136593687792069053L;
	
	private IndexIdentity indexIdentity;
	
	public NoSuchIndexException() {
		super();
	}
	
	public NoSuchIndexException(IndexIdentity indexIdentity) {
		super();
		this.indexIdentity = indexIdentity;
	}

	public IndexIdentity getIndexIdentity() {
		return indexIdentity;
	}

	public void setIndexIdentity(IndexIdentity indexIdentity) {
		this.indexIdentity = indexIdentity;
	}
	
	@Override
	public String getMessage() {
		return "Index \"" + indexIdentity + "\" does not exist";
	}

}
