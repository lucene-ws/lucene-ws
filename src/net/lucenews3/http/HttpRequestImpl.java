package net.lucenews3.http;

public class HttpRequestImpl extends HttpCommunicationImpl implements HttpRequest {

	private String method;
	private String host;
	private Integer port;
	private String resource;
	private ParameterList parameters;
	private String protocol;

	public HttpRequestImpl() {
		this.method = "GET";
		this.parameters = new ParameterListImpl();
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
	public ParameterList getParameters() {
		return parameters;
	}

	public void setParameters(ParameterList parameters) {
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
