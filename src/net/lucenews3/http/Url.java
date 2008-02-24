package net.lucenews3.http;

import java.net.URL;
import java.util.Deque;

import net.lucenews3.KeyValueList;

public interface Url {

	public Url clone();
	
	public URL toURL();
	
	public void set(URL url);
	
	public String getProtocol();
	
	public void setProtocol(String protocol);
	
	public String getHost();
	
	public void setHost(String host);
	
	public Integer getPort();
	
	public void setPort(Integer port);
	
	public Deque<String> getPath();
	
	public void setPath(Deque<String> path);
	
	public KeyValueList<String, String> getParameters();
	
	public void setParameters(KeyValueList<String, String> parameters);
	
}
