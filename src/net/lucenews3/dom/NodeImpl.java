package net.lucenews3.dom;

import java.util.List;
import java.util.Map;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.UserDataHandler;

public class NodeImpl implements Node {

	private String nodeName;
	private String nodeValue;
	private short nodeType;
	private Node parentNode;
	private NodeList childNodes;
	private Node previousSibling;
	private Node nextSibling;
	private NamedNodeMap attributes;
	private Document ownerDocument;
	private String namespaceURI;
	private String prefix;
	private String localName;
	private String baseURI;
	private String textContext;
	private Map<String, Object> userData;

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getNodeValue() {
		return nodeValue;
	}

	public void setNodeValue(String nodeValue) {
		this.nodeValue = nodeValue;
	}

	public short getNodeType() {
		return nodeType;
	}

	public void setNodeType(short nodeType) {
		this.nodeType = nodeType;
	}

	public Node getParentNode() {
		return parentNode;
	}

	public void setParentNode(Node parentNode) {
		this.parentNode = parentNode;
	}

	public NodeList getChildNodes() {
		return childNodes;
	}

	public void setChildNodes(NodeList childNodes) {
		this.childNodes = childNodes;
	}

	public Node getPreviousSibling() {
		return previousSibling;
	}

	public void setPreviousSibling(Node previousSibling) {
		this.previousSibling = previousSibling;
	}

	public Node getNextSibling() {
		return nextSibling;
	}

	public void setNextSibling(Node nextSibling) {
		this.nextSibling = nextSibling;
	}

	public NamedNodeMap getAttributes() {
		return attributes;
	}

	public void setAttributes(NamedNodeMap attributes) {
		this.attributes = attributes;
	}

	public Document getOwnerDocument() {
		return ownerDocument;
	}

	public void setOwnerDocument(Document ownerDocument) {
		this.ownerDocument = ownerDocument;
	}

	public String getNamespaceURI() {
		return namespaceURI;
	}

	public void setNamespaceURI(String namespaceURI) {
		this.namespaceURI = namespaceURI;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getLocalName() {
		return localName;
	}

	public void setLocalName(String localName) {
		this.localName = localName;
	}

	public String getBaseURI() {
		return baseURI;
	}

	public void setBaseURI(String baseURI) {
		this.baseURI = baseURI;
	}

	public String getTextContext() {
		return textContext;
	}

	public void setTextContext(String textContext) {
		this.textContext = textContext;
	}

	public Map<String, Object> getUserData() {
		return userData;
	}

	public void setUserData(Map<String, Object> userData) {
		this.userData = userData;
	}

	@Override
	public org.w3c.dom.Node appendChild(org.w3c.dom.Node node)
			throws DOMException {
		final List<org.w3c.dom.Node> childNodes = getChildNodes();
		childNodes.add(node);
		return node;
	}

	@Override
	public org.w3c.dom.Node cloneNode(boolean flag) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public short compareDocumentPosition(org.w3c.dom.Node node)
			throws DOMException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getFeature(String s, String s1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public org.w3c.dom.Node getFirstChild() {
		final NodeList childNodes = getChildNodes();
		return childNodes.get(0);
	}

	@Override
	public org.w3c.dom.Node getLastChild() {
		final NodeList childNodes = getChildNodes();
		return childNodes.get(childNodes.size() - 1);
	}

	@Override
	public String getTextContent() throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getUserData(String s) {
		return userData.get(s);
	}

	@Override
	public boolean hasAttributes() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasChildNodes() {
		final NodeList childNodes = getChildNodes();
		return !childNodes.isEmpty();
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
