package net.lucenews3.atom;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Node;

public class InlineXhtmlContentImpl extends ContentImpl implements InlineXhtmlContent {

	private List<Node> contentNodes;
	
	public InlineXhtmlContentImpl() {
		setType("xhtml");
		this.contentNodes = new ArrayList<Node>();
	}
	
	@Override
	public List<Node> getContentNodes() {
		return contentNodes;
	}

	@Override
	public void setContentNodes(List<Node> contentNodes) {
		this.contentNodes = contentNodes;
	}

}
