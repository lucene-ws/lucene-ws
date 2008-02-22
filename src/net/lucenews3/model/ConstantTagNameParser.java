package net.lucenews3.model;

public class ConstantTagNameParser<I> implements Parser<I, String> {

	private String tagName;
	
	public ConstantTagNameParser() {
		
	}
	
	public ConstantTagNameParser(String tagName) {
		this.tagName = tagName;
	}
	
	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	@Override
	public String parse(I input) {
		return tagName;
	}
	
}
