package net.lucenews3.model;

import java.io.IOException;
import java.util.Set;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.Similarity;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.Weight;

public class FilterTermQueryImpl extends TermQuery implements FilterTermQuery {

	private static final long serialVersionUID = 4478431055729259114L;
	private TermQuery target;
	
	public FilterTermQueryImpl(TermQuery target) {
		super(target.getTerm());
		this.target = target;
	}
	
	@Override
	public TermQuery getTarget() {
		return target;
	}

	public void setTarget(TermQuery target) {
		this.target = target;
	}

	@Override
	public Object clone() {
		return target.clone();
	}

	@Override
	public Query combine(Query[] queries) {
		return target.combine(queries);
	}

	@Override
	public boolean equals(Object o) {
		return target.equals(o);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void extractTerms(Set terms) {
		target.extractTerms(terms);
	}

	@Override
	public float getBoost() {
		return target.getBoost();
	}

	@Override
	public Similarity getSimilarity(Searcher searcher) {
		return target.getSimilarity(searcher);
	}

	@Override
	public Term getTerm() {
		return target.getTerm();
	}

	@Override
	public int hashCode() {
		return target.hashCode();
	}

	@Override
	public Query rewrite(IndexReader reader) throws IOException {
		return target.rewrite(reader);
	}

	@Override
	public void setBoost(float b) {
		target.setBoost(b);
	}

	@Override
	public String toString() {
		return target.toString();
	}

	@Override
	public String toString(String field) {
		return target.toString(field);
	}

	@Override
	public Weight weight(Searcher searcher) throws IOException {
		return target.weight(searcher);
	}

}
