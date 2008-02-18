package net.lucenews3.atom;

import java.util.List;

import org.w3c.dom.Node;

public interface InlineXhtmlContent extends Content {

	public List<Node> getContentNodes();
	
	public void setContentNodes(List<Node> nodes);
	
}
