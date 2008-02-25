package net.lucenews3.opensearch.dom4j;

import net.lucenews3.atom.Entry;
import net.lucenews3.atom.EntryList;
import net.lucenews3.atom.Feed;
import net.lucenews3.atom.FeedImpl;
import net.lucenews3.atom.dom4j.AbstractBuilder;
import net.lucenews3.atom.dom4j.FeedBuilder;
import net.lucenews3.model.OpenSearchResultToEntryTransformer;
import net.lucenews3.opensearch.Query;
import net.lucenews3.opensearch.QueryList;
import net.lucenews3.opensearch.Response;
import net.lucenews3.opensearch.Result;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;

public class ResponseBuilder extends AbstractBuilder {
	
	private FeedBuilder feedBuilder;
	private QueryBuilder queryBuilder;
	private Namespace opensearchNamespace;
	private Namespace atomNamespace;
	
	public ResponseBuilder() {
		this.feedBuilder = new FeedBuilder();
		this.queryBuilder = new QueryBuilder();
		this.opensearchNamespace = Namespace.get("opensearch", "http://a9.com/-/spec/opensearch/1.1/");
		this.atomNamespace = Namespace.get("atom", "http://www.w3.org/2005/Atom");
	}
	
	public Element build(Response response) {
		final Element element = DocumentHelper.createElement(QName.get("feed", atomNamespace));
		element.add(opensearchNamespace);
		build(response, element);
		return element;
	}

	public void build(Response response, Element element) {
		final Feed feed = new FeedImpl();
		feed.setTitle(response.getTitle());
		feed.setUpdated(response.getUpdated());
		feed.getLinks().addAll(response.getLinks());
		feed.setLogo("http://www.lucene-ws.net/images/magnifying_glass.png");
		
		final EntryList entries = feed.getEntries();
		for (Result result : response.getResults()) {
			final Entry entry = new OpenSearchResultToEntryTransformer().transform(result);
			entries.add(entry);
		}
		
		addPropertyNS(element, "http://a9.com/-/spec/opensearch/1.1/", "opensearch:totalResults", response.getTotalResults());
		addPropertyNS(element, "http://a9.com/-/spec/opensearch/1.1/", "opensearch:startIndex", response.getStartIndex(), false);
		
		final QueryList queries = response.getQueries();
		for (Query query : queries) {
			element.add(queryBuilder.build(query));
		}
		
		feedBuilder.build(feed, element);
	}

	public Namespace getOpensearchNamespace() {
		return opensearchNamespace;
	}

	public void setOpensearchNamespace(Namespace opensearchNamespace) {
		this.opensearchNamespace = opensearchNamespace;
	}

	public Namespace getAtomNamespace() {
		return atomNamespace;
	}

	public void setAtomNamespace(Namespace atomNamespace) {
		this.atomNamespace = atomNamespace;
	}
	
}
