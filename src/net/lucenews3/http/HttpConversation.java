package net.lucenews3.http;

public interface HttpConversation {

	public HttpRequest getRequest();
	
	public void setRequest(HttpRequest request);
	
	public HttpResponse getResponse();
	
	public void setResponse(HttpResponse response);
	
}
