package net.lucenews.http;

import java.nio.ByteBuffer;

public class DefaultHttpCommunication implements HttpCommunication {

	private HeaderCollection headers;
	private ByteBuffer body;
	
	public DefaultHttpCommunication() {
		this.headers = new DefaultHeaderCollection();
		this.body = ByteBuffer.allocate(20000);
	}
	
	@Override
	public HeaderCollection getHeaders() {
		return headers;
	}

	@Override
	public void setHeaders(HeaderCollection headers) {
		this.headers = headers;
	}

	@Override
	public ByteBuffer getBody() {
		return body;
	}

	@Override
	public void setBody(ByteBuffer body) {
		this.body = body;
	}

}
