package net.lucenews3.dom;

public interface Node extends org.w3c.dom.Node {

	@Override
	public NodeList getChildNodes();
	
}
