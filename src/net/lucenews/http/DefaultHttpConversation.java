package net.lucenews.http;

public class DefaultHttpConversation implements HttpConversation {

	private HttpRequest request;
	private HttpResponse response;

	public DefaultHttpConversation() {
		
	}

	public DefaultHttpConversation(HttpRequest request, HttpResponse response) {
		this.request = request;
		this.response = response;
	}
	
	public HttpRequest getRequest() {
		return request;
	}

	public void setRequest(HttpRequest request) {
		this.request = request;
	}

	public HttpResponse getResponse() {
		return response;
	}

	public void setResponse(HttpResponse response) {
		this.response = response;
	}
	
}
