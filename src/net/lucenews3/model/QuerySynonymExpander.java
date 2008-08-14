package net.lucenews3.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;

public class QuerySynonymExpander {

	private MethodInvoker methodInvoker;
	private Cloner cloner;
	private SynonymSource synonymSource;
	
	public QuerySynonymExpander() {
		this.methodInvoker = new MethodInvokerImpl();
	}
	
	@SuppressWarnings("unchecked")
	public Collection<Query> getSynonyms(Query query) {
		return (Collection<Query>) methodInvoker.invoke(this, "getQuerySynonyms", query);
	}
	
	public Collection<Query> getQuerySynonyms(Query query) {
		return new ArrayList<Query>();
	}
	
	public Collection<Query> getQuerySynonyms(TermQuery query) {
		Collection<Query> results;
		
		Term term = query.getTerm();
		String word = term.text();
		Collection<String> synonyms = synonymSource.getSynonyms(word);
		results = new ArrayList<Query>(synonyms.size());
		
		for (String synonym : synonyms) {
			Term synonymTerm = new Term(term.field(), synonym);
			TermQuery synonymQuery = new TermQuery(synonymTerm);
			results.add(synonymQuery);
		}
		
		return results;
	}
	
	public Query expand(Query query) {
		return (Query) methodInvoker.invoke(this, "expandQuery", query);
	}
	
	public Query expandQuery(TermQuery query) throws IOException {
		Term term = query.getTerm();
		
		String word = term.text();
		Collection<String> synonyms = synonymSource.getSynonyms(word);
		
		BooleanQuery expanded = new BooleanQuery();
		expanded.add(query, BooleanClause.Occur.SHOULD);
		
		for (String synonym : synonyms) {
			Term synonymTerm = new Term(term.field(), synonym);
			TermQuery synonymQuery = new TermQuery(synonymTerm);
			expanded.add(synonymQuery, BooleanClause.Occur.SHOULD);
		}
		
		return expanded;
	}

	public MethodInvoker getMethodInvoker() {
		return methodInvoker;
	}

	public void setMethodInvoker(MethodInvoker methodInvoker) {
		this.methodInvoker = methodInvoker;
	}

	public Cloner getCloner() {
		return cloner;
	}

	public void setCloner(Cloner cloner) {
		this.cloner = cloner;
	}

	public SynonymSource getSynonymSource() {
		return synonymSource;
	}

	public void setSynonymSource(SynonymSource synonymSource) {
		this.synonymSource = synonymSource;
	}
	
}
