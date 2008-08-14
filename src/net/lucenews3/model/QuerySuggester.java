package net.lucenews3.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;

public class QuerySuggester {

	private MethodInvoker methodInvoker;
	private Cloner cloner;
	
	public QuerySuggester() {
		this.methodInvoker = new MethodInvokerImpl();
		this.cloner = new ClonerImpl();
	}
	
	@SuppressWarnings("unchecked")
	public List<Query> suggest(Query query) {
		return (List<Query>) methodInvoker.invoke(this, "suggestQueries", query);
	}
	
	public List<Query> asList(Query... queries) {
		return Arrays.asList(queries);
	}
	
	public List<Query> suggestQueries(Query query) {
		return asList(query);
	}
	
	public List<Query> suggestQueries(TermQuery query) {
		return asList(query);
	}
	
	@SuppressWarnings("unchecked")
	public List<Query> suggestQueries(BooleanQuery query) {
		List<Query> results = new ArrayList<Query>();
		
		BooleanClause[] clauses = query.getClauses();
		
		List<Query>[] suggestedQueriesByIndex = (List<Query>[]) new List[clauses.length];
		for (int i = 0; i < clauses.length; i++) {
			BooleanClause clause = clauses[i];
			List<Query> suggestedQueries = suggest(clause.getQuery());
			suggestedQueriesByIndex[i] = suggestedQueries;
		}
		
		for (List<Query> permutation : new PermutationCollection<Query>(suggestedQueriesByIndex)) {
			BooleanQuery alternative = cloner.clone(query);
			
			ListIterator<Query> iterator = permutation.listIterator();
			while (iterator.hasNext()) {
				int index = iterator.nextIndex();
				Query suggestedQuery = iterator.next();
				BooleanClause clause = new BooleanClause(suggestedQuery, clauses[index].getOccur());
				alternative.add(clause);
			}
		}
		
		return results;
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
	
}
