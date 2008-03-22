package net.lucenews3.opensearch.transform;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.queryParser.Token;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;

import net.lucenews3.Transformer;
import net.lucenews3.atom.Link;
import net.lucenews3.atom.LinkImpl;
import net.lucenews3.atom.dom4j.LinkBuilder;
import net.lucenews3.http.Url;
import net.lucenews3.model.IndexIdentity;
import net.lucenews3.model.IndexRange;
import net.lucenews3.model.Page;
import net.lucenews3.model.Result;
import net.lucenews3.model.ResultList;
import net.lucenews3.model.SearchContext;
import net.lucenews3.model.SearchRequest;
import net.lucenews3.model.TokenSource;
import net.lucenews3.opensearch.Query;
import net.lucenews3.opensearch.QueryImpl;
import net.lucenews3.opensearch.dom4j.QueryBuilder;

public class OpenSearchContextTransformer implements Transformer<SearchContext, Document> {

	private Namespace atomNamespace;
	private Namespace openSearchNamespace;
	private Namespace relevanceNamespace;
	private Namespace xhtmlNamespace;
	private LinkBuilder linkBuilder;
	private QueryBuilder queryBuilder;
	private OpenSearchResultTransformer resultTransformer;
	
	public OpenSearchContextTransformer() {
		this.linkBuilder = new LinkBuilder();
		this.queryBuilder = new QueryBuilder();
		this.resultTransformer = new OpenSearchResultTransformer();
		this.relevanceNamespace = Namespace.get("relevance", "http://a9.com/-/opensearch/extensions/relevance/1.0/");
		this.xhtmlNamespace = Namespace.get("xhtml", "http://www.w3.org/1999/xhtml");
	}
	
	@Override
	public Document transform(final SearchContext context) {
		final Document document = DocumentHelper.createDocument();
		transform(context, document);
		return document;
	}

	
	
	@Override
	public void transform(final SearchContext context, final Document document) {
		final SearchRequest searchRequest = context.getSearchRequest();
		final ResultList results = context.getResults();
		final IndexRange indexRange = context.getIndexRange();
		
		addStylesheet(document, context.getBaseUrl());
		
		final Element feed = DocumentHelper.createElement(QName.get("feed", atomNamespace));
		document.add(feed);
		
		feed.add(openSearchNamespace);
		feed.add(relevanceNamespace);
		feed.add(xhtmlNamespace);
		
		// Title
		addProperty(feed, QName.get("title", atomNamespace), "Search results", true);
		
		// Total results
		addProperty(feed, QName.get("totalResults", openSearchNamespace), results.size(), false);
		
		// Start index
		addProperty(feed, QName.get("startIndex", openSearchNamespace), indexRange.fromIndex() + 1, false);
		
		// Items per page
		// TODO Items per page needs a better definition
		addProperty(feed, QName.get("itemsPerPage", openSearchNamespace), (indexRange.toIndex() - indexRange.fromIndex()), false);
		
		// Query
		final org.apache.lucene.search.Query luceneQuery = searchRequest.getQuery();
		
		String searchTerms;
		if (luceneQuery == null || luceneQuery instanceof MatchAllDocsQuery) {
			searchTerms = "";
		} else {
			if (luceneQuery instanceof TokenSource) {
				final TokenSource tokenSource = (TokenSource) luceneQuery;
				final Token token = tokenSource.getToken();
				searchTerms = token.image;
			} else {
				searchTerms = luceneQuery.toString();
			}
		}
		
		final Query query = new QueryImpl();
		query.setRole("request");
		query.setSearchTerms(searchTerms);
		feed.add(queryBuilder.build(query));
		
		// Links
		addLinks(context, feed);

		// Results
		List<Result> displayedResults = results.subList(indexRange.fromIndex(), Math.min(indexRange.toIndex(), results.size()));
		for (Result displayedResult : displayedResults) {
			feed.add(resultTransformer.transform(context, displayedResult));
		}
	}

	public void addLinks(final SearchContext context, final Element feed) {
		addSelfLink(context, feed);
		addFirstLink(context, feed);
		addPreviousLink(context, feed);
		addNextLink(context, feed);
		addLastLink(context, feed);
		addSearchLink(context, feed);
	}
	
	public void addSelfLink(final SearchContext context, final Element feed) {
		final Link selfLink = new LinkImpl();
		selfLink.setRel("self");
		selfLink.setHref(context.getUrl().toString());
		selfLink.setType("application/atom+xml");
		feed.add(linkBuilder.build(selfLink));
	}
	
	public void addFirstLink(final SearchContext context, final Element feed) {
		final Url firstUrl = context.getUrl().clone();
		
		final IndexRange indexRange = context.getIndexRange();
		if (indexRange instanceof Page) {
			firstUrl.getParameters().byKey().put("page", String.valueOf(1));
		} else {
			firstUrl.getParameters().byKey().put("startIndex", String.valueOf(1));
		}
		
		final Link firstLink = new LinkImpl();
		firstLink.setRel("first");
		firstLink.setHref(firstUrl.toString());
		firstLink.setType("application/atom+xml");
		feed.add(linkBuilder.build(firstLink));
	}
	
	public void addPreviousLink(final SearchContext context, final Element feed) {
		final Url previousUrl = context.getUrl().clone();
		
		final IndexRange indexRange = context.getIndexRange();
		if (indexRange instanceof Page) {
			final Page page = (Page) indexRange;
			final int currentOrdinal = page.getOrdinal();
			if (currentOrdinal > 1) {
				previousUrl.getParameters().byKey().put("page", String.valueOf(currentOrdinal - 1));
			} else {
				// No previous link
				return;
			}
		} else {
			if (indexRange.fromIndex() > 0) {
				// TODO
				final int size = (indexRange.toIndex() - indexRange.fromIndex()); 
				previousUrl.getParameters().byKey().put("startIndex", String.valueOf(indexRange.fromIndex() - size));
			} else {
				// No previous link
				return;
			}
		}
		
		final Link previousLink = new LinkImpl();
		previousLink.setRel("previous");
		previousLink.setHref(previousLink.toString());
		previousLink.setType("application/atom+xml");
		feed.add(linkBuilder.build(previousLink));
	}
	
	public void addNextLink(final SearchContext context, final Element feed) {
		final Url nextUrl = context.getUrl().clone();
		
		final IndexRange indexRange = context.getIndexRange();
		if (indexRange instanceof Page) {
			final Page page = (Page) indexRange;
			final int currentOrdinal = page.getOrdinal();
			nextUrl.getParameters().byKey().put("page", String.valueOf(currentOrdinal + 1));
		} else {
			// TODO
			nextUrl.getParameters().byKey().put("startIndex", String.valueOf(indexRange.toIndex()));
		}
		
		final Link nextLink = new LinkImpl();
		nextLink.setRel("next");
		nextLink.setHref(nextUrl.toString());
		nextLink.setType("application/atom+xml");
		feed.add(linkBuilder.build(nextLink));
	}
	
	public void addLastLink(final SearchContext context, final Element feed) {
		final Url lastUrl = context.getUrl().clone();
		
		final IndexRange indexRange = context.getIndexRange();
		if (indexRange instanceof Page) {
			lastUrl.getParameters().byKey().put("page", String.valueOf(1));
		} else {
			lastUrl.getParameters().byKey().put("startIndex", String.valueOf(1));
		}
		
		final Link lastLink = new LinkImpl();
		lastLink.setRel("last");
		lastLink.setHref(lastUrl.toString());
		lastLink.setType("application/atom+xml");
		feed.add(linkBuilder.build(lastLink));
	}
	
	public void addSearchLink(final SearchContext context, final Element feed) {
		final IndexIdentity indexIdentity = context.getIndexIdentity();
		final Url searchUrl = context.getBaseUrl().clone();
		searchUrl.getPath().add(indexIdentity.toString());
		searchUrl.getPath().add("opensearchdescription.xml");
		
		final Link searchLink = new LinkImpl();
		searchLink.setRel("search");
		searchLink.setHref(searchUrl.toString());
		searchLink.setType("application/opensearchdescription+xml");
		feed.add(linkBuilder.build(searchLink));
	}
	
	protected void addProperty(final Element parent, final String name, final Object value, final boolean required) {
		if (value == null) {
			if (required) {
				final Element child = DocumentHelper.createElement(name);
				parent.add(child);
				
				child.add(DocumentHelper.createText(""));
			}
		} else {
			final Element child = DocumentHelper.createElement(name);
			parent.add(child);
			
			child.add(DocumentHelper.createText(value.toString()));
		}
	}

	protected void addProperty(final Element parent, final QName name, final Object value, final boolean required) {
		if (value == null) {
			if (required) {
				final Element child = DocumentHelper.createElement(name);
				parent.add(child);
				
				child.add(DocumentHelper.createText(""));
			}
		} else {
			final Element child = DocumentHelper.createElement(name);
			parent.add(child);
			
			child.add(DocumentHelper.createText(value.toString()));
		}
	}
	
	/**
	 * Attaches the search results style sheet to the given document.
	 * @param document
	 * @param baseUrl
	 */
	public void addStylesheet(final Document document, final Url baseUrl) {
		final Url stylesheetUrl = baseUrl.clone();
		stylesheetUrl.getPath().add("static");
		
		// TODO Standardize style sheet
		//stylesheetUrl.getPath().add("opensearch_to_html.xsl");
		stylesheetUrl.getPath().add("searchResults.xslt");
		
		final Map<String, String> data = new LinkedHashMap<String, String>();
		data.put("type", "text/xsl");
		data.put("href", stylesheetUrl.toString());
		
		document.add(DocumentHelper.createProcessingInstruction("xml-stylesheet", data));
	}
	
	public Namespace getAtomNamespace() {
		return atomNamespace;
	}

	public void setAtomNamespace(Namespace atomNamespace) {
		this.atomNamespace = atomNamespace;
	}

	public Namespace getOpenSearchNamespace() {
		return openSearchNamespace;
	}

	public void setOpenSearchNamespace(Namespace openSearchNamespace) {
		this.openSearchNamespace = openSearchNamespace;
	}

}
