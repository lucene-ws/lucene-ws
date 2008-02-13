package net.lucenews3.dom;

import java.util.AbstractList;

import org.w3c.dom.Node;

public class NodeListImpl extends AbstractList<org.w3c.dom.Node> implements NodeList {

	private org.w3c.dom.NodeList nodes;
	
	public NodeListImpl(org.w3c.dom.NodeList nodes) {
		this.nodes = nodes;
	}
	
	@Override
	public Node get(int index) {
		return nodes.item(index);
	}

	@Override
	public int size() {
		return nodes.getLength();
	}

	@Override
	public int getLength() {
		return nodes.getLength();
	}

	@Override
	public Node item(int index) {
		return nodes.item(index);
	}

}
