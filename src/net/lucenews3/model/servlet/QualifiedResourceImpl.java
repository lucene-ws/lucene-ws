package net.lucenews3.model.servlet;

public class QualifiedResourceImpl<R> implements QualifiedResource<R> {

	private Path path;
	private R resource;
	
	public QualifiedResourceImpl() {
		
	}
	
	public QualifiedResourceImpl(Path path, R resource) {
		this.path = path;
		this.resource = resource;
	}
	
	@Override
	public Path getPath() {
		return path;
	}
	
	public void setPath(Path path) {
		this.path = path;
	}

	@Override
	public R getResource() {
		return resource;
	}
	
	public void setResource(R resource) {
		this.resource = resource;
	}

}
