package net.lucenews3.http;

import java.nio.ByteBuffer;

public interface HttpCommunication {
	
	public HeaderList getHeaders();
	
	public void setHeaders(HeaderList headers);
	
	public ByteBuffer getBody();
	
	public void setBody(ByteBuffer body);

}
