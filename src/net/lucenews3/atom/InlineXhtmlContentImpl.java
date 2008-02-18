package net.lucenews3.atom;

import java.util.List;

import org.w3c.dom.Node;

public class InlineXhtmlContentImpl extends ContentImpl implements InlineXhtmlContent {

	private List<Node> contentNodes;
	
	@Override
	public List<Node> getContentNodes() {
		return contentNodes;
	}

	@Override
	public void setContentNodes(List<Node> contentNodes) {
		this.contentNodes = contentNodes;
	}

}
