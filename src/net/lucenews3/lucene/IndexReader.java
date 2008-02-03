package net.lucenews3.lucene;

import java.util.Collection;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.FieldSelector;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermDocs;
import org.apache.lucene.index.TermEnum;
import org.apache.lucene.index.TermFreqVector;
import org.apache.lucene.index.TermPositions;
import org.apache.lucene.index.TermVectorMapper;
import org.apache.lucene.store.Directory;

import net.lucenews3.Closeable;

public interface IndexReader extends NativeImplementationProvider<org.apache.lucene.index.IndexReader>, Closeable {
	
	public void deleteDocument(int documentNumber);
	
	public int deleteDocuments(Term term);
	
	public Directory directory();
	
	public int docFreq(Term term);
	
	public Document document(int n);
	
	public Document document(int n, FieldSelector fieldSelector);
	
	public void flush();
	
	public Collection<String> getFieldNames(org.apache.lucene.index.IndexReader.FieldOption fldOption);
	
	public TermFreqVector getTermFreqVector(int docNumber, String field);
	
	public TermFreqVector getTermFreqVector(int docNumber, String field, TermVectorMapper mapper);
	
	public TermFreqVector getTermFreqVector(int docNumber, TermVectorMapper mapper);
	
	public TermFreqVector[] getTermFreqVectors(int docNumber);
	
	public int getTermInfosIndexDivisor();
	
	public long getVersion();
	
	public boolean hasDeletions();
	
	public boolean hasNorms(String field);
	
	public boolean isCurrent();
	
	public boolean isDeleted(int n);
	
	public boolean isOptimized();
	
	public int maxDoc();
	
	public byte[] norms(String field);
	
	public byte[] norms(String field, byte[] bytes, int offset);
	
	public int numDocs();
	
	public IndexReader reopen();
	
	public void setNorm(int doc, String field, byte value);
	
	public TermDocs termDocs();
	
	public TermDocs termDocs(Term term);
	
	public TermPositions termPositions();
	
	public TermPositions termPositions(Term term);
	
	public TermEnum terms();
	
	public TermEnum terms(Term t);
	
	public void undeleteAll();
	
}
