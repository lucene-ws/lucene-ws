package net.lucenews.http;

import java.nio.ByteBuffer;

public class DefaultHttpRequest implements HttpRequest {

	private String method;
	private ParameterCollection parameters;
	private String protocol;
	private HeaderCollection headers;
	private ByteBuffer body;

	@Override
	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	@Override
	public ParameterCollection getParameters() {
		return parameters;
	}

	public void setParameters(ParameterCollection parameters) {
		this.parameters = parameters;
	}

	@Override
	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	@Override
	public HeaderCollection getHeaders() {
		return headers;
	}

	public void setHeaders(HeaderCollection headers) {
		this.headers = headers;
	}

	@Override
	public ByteBuffer getBody() {
		return body;
	}

	public void setBody(ByteBuffer body) {
		this.body = body;
	}

}
