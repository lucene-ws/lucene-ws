package net.lucenews3.http;

import java.nio.ByteBuffer;

public class HttpCommunicationImpl implements HttpCommunication {

	private HeaderList headers;
	private ByteBuffer body;
	
	public HttpCommunicationImpl() {
		this.headers = new HeaderListImpl();
		this.body = ByteBuffer.allocate(20000);
	}
	
	@Override
	public HeaderList getHeaders() {
		return headers;
	}

	@Override
	public void setHeaders(HeaderList headers) {
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
