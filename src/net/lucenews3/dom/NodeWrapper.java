package net.lucenews3.dom;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.UserDataHandler;

public class NodeWrapper implements Node {

	private org.w3c.dom.Node node;
	
	public NodeWrapper(org.w3c.dom.Node node) {
		this.node = node;
	}
	
	@Override
	public NodeList getChildNodes() {
		return new ChildNodeList(node);
	}

	@Override
	public org.w3c.dom.Node appendChild(org.w3c.dom.Node node)
			throws DOMException {
		return this.node.appendChild(node);
	}

	@Override
	public org.w3c.dom.Node cloneNode(boolean flag) {
		return node.cloneNode(flag);
	}

	@Override
	public short compareDocumentPosition(org.w3c.dom.Node node)
			throws DOMException {
		return this.node.compareDocumentPosition(node);
	}

	@Override
	public NamedNodeMap getAttributes() {
		return node.getAttributes();
	}

	@Override
	public String getBaseURI() {
		return node.getBaseURI();
	}

	@Override
	public Object getFeature(String s, String s1) {
		return node.getFeature(s, s1);
	}

	@Override
	public org.w3c.dom.Node getFirstChild() {
		return node.getFirstChild();
	}

	@Override
	public org.w3c.dom.Node getLastChild() {
		return node.getLastChild();
	}

	@Override
	public String getLocalName() {
		return node.getLocalName();
	}

	@Override
	public String getNamespaceURI() {
		return node.getNamespaceURI();
	}

	@Override
	public org.w3c.dom.Node getNextSibling() {
		return node.getNextSibling();
	}

	@Override
	public String getNodeName() {
		return node.getNodeName();
	}

	@Override
	public short getNodeType() {
		return node.getNodeType();
	}

	@Override
	public String getNodeValue() throws DOMException {
		return node.getNodeValue();
	}

	@Override
	public Document getOwnerDocument() {
		return node.getOwnerDocument();
	}

	@Override
	public org.w3c.dom.Node getParentNode() {
		return node.getParentNode();
	}

	@Override
	public String getPrefix() {
		return node.getPrefix();
	}

	@Override
	public org.w3c.dom.Node getPreviousSibling() {
		return node.getPreviousSibling();
	}

	@Override
	public String getTextContent() throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getUserData(String s) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasAttributes() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasChildNodes() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public org.w3c.dom.Node insertBefore(org.w3c.dom.Node node,
			org.w3c.dom.Node node1) throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isDefaultNamespace(String s) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEqualNode(org.w3c.dom.Node node) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSameNode(org.w3c.dom.Node node) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSupported(String s, String s1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String lookupNamespaceURI(String s) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String lookupPrefix(String s) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void normalize() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public org.w3c.dom.Node removeChild(org.w3c.dom.Node node)
			throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public org.w3c.dom.Node replaceChild(org.w3c.dom.Node node,
			org.w3c.dom.Node node1) throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setNodeValue(String s) throws DOMException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPrefix(String s) throws DOMException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTextContent(String s) throws DOMException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object setUserData(String s, Object obj,
			UserDataHandler userdatahandler) {
		// TODO Auto-generated method stub
		return null;
	}

}
