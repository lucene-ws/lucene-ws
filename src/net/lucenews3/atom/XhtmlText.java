package net.lucenews3.atom;

import java.util.Collection;

import org.w3c.dom.Node;

public interface XhtmlText extends Text {

	public Collection<Node> getContentNodes();
	
	public void setContentNodes(Collection<Node> contentNodes);
	
}
