package net.lucenews3.http;

import java.net.URL;
import java.util.ArrayDeque;
import java.util.Deque;

import net.lucenews3.KeyValueList;
import net.lucenews3.KeyValueListImpl;

public class UrlImpl implements Url {
	
	private String protocol;
	private String host;
	private Integer port;
	private Deque<String> path;
	private KeyValueList<String, String> parameters;

	public UrlImpl() {
		
	}
	
	public UrlImpl(Url url) {
		this.protocol = url.getProtocol();
		this.host = url.getHost();
		this.port = url.getPort();
		this.path = url.getPath();
		this.parameters = url.getParameters();
	}
	
	public UrlImpl clone() {
		final UrlImpl result = new UrlImpl(this);
		final Deque<String> resultPath = new ArrayDeque<String>();
		resultPath.addAll(this.path);
		result.setPath(resultPath);
		final KeyValueList<String, String> resultParameters = new KeyValueListImpl<String, String>(this.parameters);
		result.parameters = resultParameters;
		return result;
	}
	
	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
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

	public Deque<String> getPath() {
		return path;
	}

	public void setPath(Deque<String> path) {
		this.path = path;
	}

	public KeyValueList<String, String> getParameters() {
		return parameters;
	}

	public void setParameters(KeyValueList<String, String> parameters) {
		this.parameters = parameters;
	}

	@Override
	public void set(URL url) {
		this.protocol = url.getProtocol();
		this.host = url.getHost();
		this.port = url.getPort();
		if (this.port < 0) {
			this.port = null;
		}
		// TODO: Path
		// TODO: Parameters
	}

	@Override
	public URL toURL() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
