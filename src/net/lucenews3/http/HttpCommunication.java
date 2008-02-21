package net.lucenews3.http;

import java.nio.ByteBuffer;

public interface HttpCommunication {
	
	public HeaderCollection getHeaders();
	
	public void setHeaders(HeaderCollection headers);
	
	public ByteBuffer getBody();
	
	public void setBody(ByteBuffer body);

}
