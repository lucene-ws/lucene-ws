package net.lucenews.http;

public interface HttpResponse extends HttpCommunication {

	public Integer getStatus();
	
	public void setStatus(Integer status);
	
	public String getMessage();
	
	public void setMessage(String message);
	
}
