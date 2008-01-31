package net.lucenews.atom;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Workspace {

	private List<AtomCollection> m_collections;

	private String m_title;

	public Workspace(String title) {
		m_collections = new LinkedList<AtomCollection>();
		setTitle(title);
	}

	public String getTitle() {
		return m_title;
	}

	public void setTitle(String title) {
		m_title = title;
	}

	public List<AtomCollection> getCollections() {
		return m_collections;
	}

	public void addCollection(AtomCollection collection) {
		m_collections.add(collection);
	}

	public Element asElement(Document document) {
		Element workspace = document.createElement("workspace");

		Element title = document.createElement("atom:title");
		title.appendChild(document.createTextNode(String.valueOf(getTitle())));
		workspace.appendChild(title);

		Iterator<AtomCollection> collections = getCollections().iterator();
		while (collections.hasNext())
			workspace.appendChild(collections.next().asElement(document));

		return workspace;
	}

}
