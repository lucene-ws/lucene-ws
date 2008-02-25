package net.lucenews3.http;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

import net.lucenews3.KeyValue;
import net.lucenews3.KeyValueImpl;
import net.lucenews3.KeyValueList;
import net.lucenews3.KeyValueListImpl;

public class UrlImpl implements Url {
	
	private String protocol;
	private String host;
	private Integer port;
	private Deque<String> path;
	private KeyValueList<String, String> parameters;

	public UrlImpl() {
		this.path = new ArrayDeque<String>();
		this.parameters = new KeyValueListImpl<String, String>();
	}
	
	public UrlImpl(String url) throws MalformedURLException {
		this(new URL(url));
	}
	
	public UrlImpl(Url url) {
		this.protocol = url.getProtocol();
		this.host = url.getHost();
		this.port = url.getPort();
		this.path = url.getPath();
		this.parameters = url.getParameters();
	}
	
	public UrlImpl(URL url) {
		this();
		set(url);
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
		String path = url.getPath();
		if (path == null || path.equals("")) {
			// Do nothing with this path
		} else {
			List<String> split = new ArrayList<String>(Arrays.asList(path.split("/")));
			if (!split.isEmpty()) {
				split.remove(0);
			}
			this.path.addAll(split);
		}
		
		// TODO: Parameters
		String query = url.getQuery();
		if (query == null || query.equals("")) {
			// Do nothing with this
		} else {
			if (query.startsWith("?")) {
				query = query.substring(1);
			}
			
			List<String> pairs = Arrays.asList(query.split("&"));
			for (String pair : pairs) {
				int index = pair.indexOf('=');
				String name = pair.substring(0, index);
				String value = pair.substring(index + 1);
				this.parameters.add(new KeyValueImpl<String, String>(name, value));
			}
		}
	}

	@Override
	public URL toURL() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(protocol);
		buffer.append("://");
		buffer.append(host);
		if (port != null) {
			buffer.append(":");
			buffer.append(port);
		}
		
		for (String p : path) {
			buffer.append("/");
			buffer.append(p);
		}
		
		if (!parameters.isEmpty()) {
			buffer.append("?");
			boolean isFirst = true;
			for (KeyValue<String, String> parameter : parameters) {
				if (isFirst) {
					isFirst = false;
				} else {
					buffer.append("&");
				}
				buffer.append(escapeParameterName(parameter.getKey()));
				buffer.append("=");
				buffer.append(escapeParameterValue(parameter.getValue()));
			}
		}
		
		return buffer.toString();
	}
	
	protected String escapeParameterName(String name) {
		// TODO
		return name;
	}
	
	protected String escapeParameterValue(String value) {
		// TODO
		return value;
	}
	
}
