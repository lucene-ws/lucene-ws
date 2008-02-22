package net.lucenews3.http;

import java.nio.ByteBuffer;

public class HttpCommunicationImpl implements HttpCommunication {

	private HeaderCollection headers;
	private ByteBuffer body;
	
	public HttpCommunicationImpl() {
		this.headers = new HeaderCollectionImpl();
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