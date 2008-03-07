package net.lucenews3.model;

import java.io.IOException;

import net.lucenews3.ExceptionTranslator;
import net.lucenews3.ExceptionTranslatorImpl;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;

public class ReferencedNativeDocument implements Document {

	private IndexReader indexReader;
	private int documentId;
	private Document target;
	private ExceptionTranslator exceptionTranslator;
	
	public ReferencedNativeDocument(IndexReader indexReader, int documentId) {
		this.indexReader = indexReader;
		this.documentId = documentId;
		this.exceptionTranslator = new ExceptionTranslatorImpl();
	}
	
	public boolean initialize() {
		boolean result;
		
		if (target == null) {
			try {
				target = new NativeDocumentDocument(indexReader.document(documentId));
			} catch (CorruptIndexException e) {
				throw exceptionTranslator.translate(e);
			} catch (IOException e) {
				throw exceptionTranslator.translate(e);
			}
			result = true;
		} else {
			result = false;
		}
		
		return result;
	}
	
	@Override
	public float getBoost() {
		initialize();
		return target.getBoost();
	}

	@Override
	public FieldList getFields() {
		initialize();
		return target.getFields();
	}

	@Override
	public void setBoost(float boost) {
		initialize();
		target.setBoost(boost);
	}

	@Override
	public void setFields(FieldList fields) {
		initialize();
		target.setFields(fields);
	}

	@Override
	public org.apache.lucene.document.Document asNative() {
		initialize();
		return target.asNative();
	}

}
