package net.lucenews3.atom;

public class ContentImpl extends CommonImpl implements Content {

	private String type;
	
	@Override
	public String getType() {
		return type;
	}

	@Override
	public void setType(String type) {
		this.type = type;
	}
	
}
