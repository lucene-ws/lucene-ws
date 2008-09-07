package net.lucenews3;

import java.util.Date;

import org.apache.lucene.document.Document;

public class DefaultDocumentMetaData implements DocumentMetaData {

	private Index index;
	private Document document;
	private String identity;
	private Date lastUpdated;

	public DefaultDocumentMetaData() {
		this((Document) null);
	}

	public DefaultDocumentMetaData(Document document) {
		this.document = document;
	}

	public DefaultDocumentMetaData(Index index, Document document) {
		this.index = index;
		this.document = document;
	}

	@Override
	public Index getIndex() {
		return index;
	}

	public void setIndex(Index index) {
		this.index = index;
	}

	@Override
	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	@Override
	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	@Override
	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

}
