package net.lucenews.http;

import java.nio.ByteBuffer;

public interface HttpRequest {
	
	public String getMethod();
	
	public void setMethod(String method);
	
	public String getProtocol();
	
	public void setProtocol(String protocol);
	
	public HeaderCollection getHeaders();
	
	public void setHeaders(HeaderCollection headers);

	public ParameterCollection getParameters();
	
	public void setParameters(ParameterCollection parameters);
	
	public ByteBuffer getBody();
	
	public void setBody(ByteBuffer body);
	
}
