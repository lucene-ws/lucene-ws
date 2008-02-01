package net.lucenews.http;

public class DefaultHttpRequest extends DefaultHttpCommunication implements HttpRequest {

	private String method;
	private ParameterCollection parameters;
	private String protocol;

	public DefaultHttpRequest() {
		this.method = "GET";
		this.parameters = new DefaultParameterCollection();
		this.protocol = "HTTP/1.0";
	}
	
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

}
