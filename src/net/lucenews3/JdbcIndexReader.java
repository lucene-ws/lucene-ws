package net.lucenews3;

import java.io.IOException;
import java.sql.Connection;
import java.util.Collection;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.FieldSelector;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermDocs;
import org.apache.lucene.index.TermEnum;
import org.apache.lucene.index.TermFreqVector;
import org.apache.lucene.index.TermPositions;
import org.apache.lucene.index.TermVectorMapper;

public class JdbcIndexReader extends IndexReader {

	private Connection con;

	@Override
	protected void doClose() throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void doCommit() throws IOException {
		// TODO Auto-generated method stub
		// TODO con.commit();
	}

	@Override
	protected void doDelete(int arg0) throws CorruptIndexException, IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void doSetNorm(int arg0, String arg1, byte arg2) throws CorruptIndexException, IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void doUndeleteAll() throws CorruptIndexException, IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int docFreq(Term term) throws IOException {
		// TODO Auto-generated method stub
		//con.prepareStatement("SELECT COUNT(*) FROM terms WHERE name = ? AND text = ?");
		return 0;
	}

	@Override
	public Document document(int arg0, FieldSelector arg1) throws CorruptIndexException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection getFieldNames(FieldOption arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TermFreqVector getTermFreqVector(int arg0, String arg1) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void getTermFreqVector(int arg0, TermVectorMapper arg1) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getTermFreqVector(int arg0, String arg1, TermVectorMapper arg2) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public TermFreqVector[] getTermFreqVectors(int arg0) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasDeletions() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isDeleted(int arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int maxDoc() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public byte[] norms(String arg0) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void norms(String arg0, byte[] arg1, int arg2) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int numDocs() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public TermDocs termDocs() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TermPositions termPositions() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TermEnum terms() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TermEnum terms(Term arg0) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
