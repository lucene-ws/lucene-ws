package net.lucenews3.opensearch;

import java.util.Calendar;
import java.util.List;

import net.lucenews.atom.Link;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public interface Result {

	public String getTitle();

	public void setTitle(String title);

	public String getId();

	public void setId(String id);

	public String getSummary();

	public void setSummary(String summary);

	public Calendar getUpdated();

	public void setUpdated(Calendar updated);

	public Float getScore();

	public void setScore(Float score);

	@Deprecated
	public Float getRelevance();

	@Deprecated
	public void setRelevance(Float relevance);

	public List<OpenSearchPerson> getPeople();

	public boolean removePerson(OpenSearchPerson person);

	public List<Link> getLinks();

	public OpenSearchText getContent();

	public void setContent(OpenSearchText content);

	public void setContent(String content);

	public void setContent(Node content);

	public void setContent(NodeList content);

	public OpenSearchText getRights();

	public void setRights(OpenSearchText rights);

}