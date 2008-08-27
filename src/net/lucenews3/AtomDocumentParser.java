package net.lucenews3;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

public class AtomDocumentParser extends AtomParser {

	protected Document document;

	public AtomDocumentParser(XMLStreamReader xml) {
		super(xml);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void parseEntry() throws XMLStreamException {
		document = new Document();
		super.parseEntry();
		// TODO export document
	}

	@Override
	protected void parseXOXOTermDefinition() throws XMLStreamException {
		Field.Store store;
		Field.Index index;
		
		if (xoxoClass == null) {
			store = Field.Store.YES;
			index = Field.Index.TOKENIZED;
		} else {
			store = Field.Store.NO;
			index = Field.Index.NO;
			
			for (String token : xoxoClass.split(",\\s*")) {
				if (token.equals("stored")) {
					store = Field.Store.YES;
				} else if (token.equals("indexed") && index == Field.Index.NO) {
					index = Field.Index.UN_TOKENIZED;
				} else if (token.equals("tokenized")) {
					index = Field.Index.TOKENIZED;
				}
			}
		}
		
		document.add(new Field(xoxoTerm, xoxoDefinition, store, index));
	}

}
