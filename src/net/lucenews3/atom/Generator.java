package net.lucenews3.atom;

public interface Generator extends Common {

	public String getUri();
	
	public void setUri(String uri);
	
	public String getVersion();
	
	public void setVersion(String version);
	
}
