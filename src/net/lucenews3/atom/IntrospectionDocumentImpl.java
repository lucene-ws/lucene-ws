package net.lucenews3.atom;

public class IntrospectionDocumentImpl implements IntrospectionDocument {

	private Service service;
	
	public IntrospectionDocumentImpl() {
		this(new ServiceImpl());
	}
	
	public IntrospectionDocumentImpl(Service service) {
		this.service = service;
	}
	
	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}

}
