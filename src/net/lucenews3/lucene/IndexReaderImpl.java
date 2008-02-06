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
import org.apache.lucene.index.IndexReader.FieldOption;
import org.apache.lucene.store.Directory;

public class IndexReaderImpl implements IndexReader {

	private org.apache.lucene.index.IndexReader nativeIndexReader;
	
	public IndexReaderImpl(org.apache.lucene.index.IndexReader nativeIndexReader) {
		this.nativeIndexReader = nativeIndexReader;
	}
	
	public void deleteDocument(int documentNumber) {
		// TODO Auto-generated method stub
		
	}

	public int deleteDocuments(Term term) {
		// TODO Auto-generated method stub
		return 0;
	}

	public Directory directory() {
		// TODO Auto-generated method stub
		return null;
	}

	public int docFreq(Term term) {
		// TODO Auto-generated method stub
		return 0;
	}

	public Document document(int n) {
		// TODO Auto-generated method stub
		return null;
	}

	public Document document(int n, FieldSelector fieldSelector) {
		// TODO Auto-generated method stub
		return null;
	}

	public void flush() {
		// TODO Auto-generated method stub
		
	}

	public Collection<String> getFieldNames(FieldOption fldOption) {
		// TODO Auto-generated method stub
		return null;
	}

	public TermFreqVector getTermFreqVector(int docNumber, String field) {
		// TODO Auto-generated method stub
		return null;
	}

	public TermFreqVector getTermFreqVector(int docNumber, String field, TermVectorMapper mapper) {
		// TODO Auto-generated method stub
		return null;
	}

	public TermFreqVector getTermFreqVector(int docNumber, TermVectorMapper mapper) {
		// TODO Auto-generated method stub
		return null;
	}

	public TermFreqVector[] getTermFreqVectors(int docNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	public int getTermInfosIndexDivisor() {
		// TODO Auto-generated method stub
		return 0;
	}

	public long getVersion() {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean hasDeletions() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean hasNorms(String field) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isCurrent() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isDeleted(int n) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isOptimized() {
		// TODO Auto-generated method stub
		return false;
	}

	public int maxDoc() {
		// TODO Auto-generated method stub
		return 0;
	}

	public byte[] norms(String field) {
		// TODO Auto-generated method stub
		return null;
	}

	public byte[] norms(String field, byte[] bytes, int offset) {
		// TODO Auto-generated method stub
		return null;
	}

	public int numDocs() {
		// TODO Auto-generated method stub
		return 0;
	}

	public IndexReader reopen() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setNorm(int doc, String field, byte value) {
		// TODO Auto-generated method stub
		
	}

	public TermDocs termDocs() {
		// TODO Auto-generated method stub
		return null;
	}

	public TermDocs termDocs(Term term) {
		// TODO Auto-generated method stub
		return null;
	}

	public TermPositions termPositions() {
		// TODO Auto-generated method stub
		return null;
	}

	public TermPositions termPositions(Term term) {
		// TODO Auto-generated method stub
		return null;
	}

	public TermEnum terms() {
		// TODO Auto-generated method stub
		return null;
	}

	public TermEnum terms(Term t) {
		// TODO Auto-generated method stub
		return null;
	}

	public void undeleteAll() {
		// TODO Auto-generated method stub
		
	}

	public org.apache.lucene.index.IndexReader asNative() {
		// TODO Auto-generated method stub
		return null;
	}

	public void close() {
		// TODO Auto-generated method stub
		
	}

}
