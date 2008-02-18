package net.lucenews3.atom;

public class InlineTextContentImpl extends ContentImpl implements InlineTextContent {

	private String text;
	
	@Override
	public String getText() {
		return text;
	}

	@Override
	public void setText(String text) {
		this.text = text;
	}

}
