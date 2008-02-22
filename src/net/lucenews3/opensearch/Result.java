package net.lucenews3.opensearch;

import java.util.Calendar;
import java.util.List;

public interface Result {

	public String getTitle();

	public void setTitle(String title);

	public String getId();

	public void setId(String id);

	public String getSummary();

	public void setSummary(String summary);

	public Calendar getUpdated();

	public void setUpdated(Calendar updated);

	public Double getRelevance();

	public void setRelevance(Double relevance);

	public List<OpenSearchPerson> getPeople();

	public boolean removePerson(OpenSearchPerson person);

	public LinkList getLinks();
	
	public void setLinks(LinkList links);

	public Content getContent();

	public void setContent(Content content);

	public Content getRights();

	public void setRights(Content rights);

}