package net.lucenews3.dom;

import java.util.AbstractList;

import org.w3c.dom.Node;

/**
 * Encapsulates a list of child nodes belonging to a particular parent node.
 *
 */
public class ChildNodeList extends AbstractList<org.w3c.dom.Node> implements NodeList {
	
	private Node parent;
	
	public ChildNodeList(Node parent) {
		this.parent = parent;
	}
	
	/**
	 * @see org.w3c.dom.Node#appendChild(Node)
	 */
	@Override
	public boolean add(Node node) {
		parent.appendChild(node);
		return true;
	}
	
	/**
	 * @see org.w3c.dom.Node#insertBefore(Node, Node)
	 * @see org.w3c.dom.Node#appendChild(Node)
	 */
	@Override
	public void add(int index, Node node) {
		if (parent.hasChildNodes()) {
			parent.insertBefore(node, parent.getChildNodes().item(index));
		} else {
			parent.appendChild(node);
		}
	}
	
	/**
	 * @see org.w3c.dom.Node#removeChild(Node)
	 * @see org.w3c.dom.Node#hasChildNodes()
	 */
	@Override
	public void clear() {
		while (parent.hasChildNodes()) {
			parent.removeChild(parent.getFirstChild());
		}
	}
	
	/**
	 * @see org.w3c.dom.Node#removeChild(Node)
	 */
	@Override
	public Node remove(int index) {
		return parent.removeChild(parent.getChildNodes().item(index));
	}
	
	/**
	 * @see org.w3c.dom.Node#removeChild(Node)
	 */
	@Override
	public boolean remove(Object object) {
		org.w3c.dom.NodeList nodes = parent.getChildNodes();
		int length = nodes.getLength();
		for (int i = 0; i < length; i++) {
			Node node = nodes.item(i);
			if (object.equals(node)) {
				parent.removeChild(node);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @see org.w3c.dom.Node#replaceChild(Node, Node)
	 */
	@Override
	public Node set(int index, Node node) {
		return parent.replaceChild(parent.getChildNodes().item(index), node);
	}
	
	/**
	 * @see org.w3c.dom.NodeList#item(int)
	 */
	@Override
	public Node get(int index) {
		return parent.getChildNodes().item(index);
	}

	/**
	 * @see org.w3c.dom.NodeList#getLength()
	 */
	@Override
	public int size() {
		return parent.getChildNodes().getLength();
	}

	@Override
	public int getLength() {
		return parent.getChildNodes().getLength();
	}

	@Override
	public Node item(int index) {
		return parent.getChildNodes().item(index);
	}

}
