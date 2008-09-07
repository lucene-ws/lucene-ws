package net.lucenews3;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

import org.apache.lucene.document.Document;

public class DefaultService implements Service {

	public static final String DEFAULT_INDEX_ATTRIBUTE_NAME = "index";

	private IndexRepository indexRepository;
	private XMLInputFactory xmlInputFactory;
	private String indexAttributeName;

	public DefaultService() {
		this.xmlInputFactory = XMLInputFactory.newInstance();
		this.indexAttributeName = DEFAULT_INDEX_ATTRIBUTE_NAME;
	}

	@Override
	public List<Index> getIndexes() throws IOException {
		return indexRepository.getIndexes();
	}

	@Override
	public Index getIndex(HttpServletRequest request) throws NoSuchIndexException, IOException {
		return indexRepository.getIndex((String) request.getAttribute(indexAttributeName));
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
	public URI getDocumentURI(HttpServletRequest request, Index index, Document document) throws URISyntaxException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public URI getIndexURI(HttpServletRequest request, Index index) throws URISyntaxException {
		return new URI(request.getContextPath() + "/" + index.getName());
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

	public String getIndexAttributeName() {
		return indexAttributeName;
	}

	public void setIndexAttributeName(String indexAttributeName) {
		this.indexAttributeName = indexAttributeName;
	}

}
