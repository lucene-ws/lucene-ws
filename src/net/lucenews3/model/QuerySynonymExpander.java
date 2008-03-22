package net.lucenews3.model;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.wordnet.SynExpand;
import org.apache.lucene.wordnet.Syns2Index;

public class QuerySynonymExpander {

	private MethodInvoker methodInvoker;
	private Cloner cloner;
	private Searcher searcher;
	private Analyzer analyzer;
	private float boost;
	private String wordFieldName;
	private String synonymFieldName;
	
	public QuerySynonymExpander() {
		this.wordFieldName = Syns2Index.F_WORD;
		this.synonymFieldName = Syns2Index.F_WORD;
	}
	
	public Query expand(Query query) {
		return (Query) methodInvoker.invoke(this, "expandQuery", query);
	}
	
	public Query expandQuery(TermQuery query) throws IOException {
		final Term term = query.getTerm();
		
		
		
		return null;
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

	public Searcher getSearcher() {
		return searcher;
	}

	public void setSearcher(Searcher searcher) {
		this.searcher = searcher;
	}

	public Analyzer getAnalyzer() {
		return analyzer;
	}

	public void setAnalyzer(Analyzer analyzer) {
		this.analyzer = analyzer;
	}

	public float getBoost() {
		return boost;
	}

	public void setBoost(float boost) {
		this.boost = boost;
	}

	public String getWordFieldName() {
		return wordFieldName;
	}

	public void setWordFieldName(String wordFieldName) {
		this.wordFieldName = wordFieldName;
	}

	public String getSynonymFieldName() {
		return synonymFieldName;
	}

	public void setSynonymFieldName(String synonymFieldName) {
		this.synonymFieldName = synonymFieldName;
	}
	
}
