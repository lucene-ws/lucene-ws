package net.lucenews3.atom;

public class OutOfLineContentImpl extends ContentImpl implements OutOfLineContent {

	private String src;
	
	@Override
	public String getSrc() {
		return src;
	}

	@Override
	public void setSrc(String src) {
		this.src = src;
	}

}
