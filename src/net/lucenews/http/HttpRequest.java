package net.lucenews.http;

public interface HttpRequest extends HttpCommunication {
	
	public String getMethod();
	
	public void setMethod(String method);
	
	public String getProtocol();
	
	public void setProtocol(String protocol);

	public ParameterCollection getParameters();
	
	public void setParameters(ParameterCollection parameters);
	
}
