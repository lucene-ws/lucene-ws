package net.lucenews.http;

public class DefaultHttpRequest extends DefaultHttpCommunication implements HttpRequest {

	private String method;
	private String host;
	private Integer port;
	private String resource;
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

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
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
