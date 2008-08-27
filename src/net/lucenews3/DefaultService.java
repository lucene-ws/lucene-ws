package net.lucenews3;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

import org.apache.lucene.document.Document;

public class DefaultService implements Service {

	private IndexRepository indexRepository;
	private XMLInputFactory xmlInputFactory;

	public DefaultService() {
		this.xmlInputFactory = XMLInputFactory.newInstance();
	}

	@Override
	public List<Index> getIndexes() throws IOException {
		return indexRepository.getIndexes();
	}

	@Override
	public Index getIndex(HttpServletRequest req) throws NoSuchIndexException, IOException {
		return indexRepository.getIndex((String) req.getAttribute("index"));
	}

	public IndexRepository getIndexRepository() {
		return indexRepository;
	}

	public void setIndexRepository(IndexRepository indexRepository) {
		this.indexRepository = indexRepository;
	}

	protected RuntimeException toRuntimeException(XMLStreamException e) {
		return new RuntimeException(e);
	}

	@Override
	public DocumentIterator getDocumentIterator(HttpServletRequest request) throws IOException {
		try {
			return new XMLDocumentIterator(xmlInputFactory.createXMLStreamReader(request.getReader()));
		} catch (XMLStreamException e) {
			throw toRuntimeException(e);
		}
	}

	@Override
	public String getDocumentURI(HttpServletRequest request, Index index, Document document) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getIndexURI(HttpServletRequest request, Index index) {
		return request.getServletPath() + index.getName();
	}

	@Override
	public String getIndexName(Index index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createIndex(String name) throws IOException {
		// TODO Auto-generated method stub
		indexRepository.createIndex(name);
	}

}
