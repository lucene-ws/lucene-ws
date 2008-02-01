package net.lucenews.http;

public interface HttpRequest extends HttpCommunication {
	
	public String getMethod();
	
	public void setMethod(String method);
	
	public String getHost();
	
	public void setHost(String host);
	
	public Integer getPort();
	
	public void setPort(Integer port);
	
	public String getResource();
	
	public void setResource(String resource);
	
	public String getProtocol();
	
	public void setProtocol(String protocol);

	public ParameterCollection getParameters();
	
	public void setParameters(ParameterCollection parameters);
	
}
