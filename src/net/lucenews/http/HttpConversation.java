package net.lucenews.http;

public interface HttpConversation {

	public HttpRequest getRequest();
	
	public void setRequest(HttpRequest request);
	
	public HttpResponse getResponse();
	
	public void setResponse(HttpResponse response);
	
}
