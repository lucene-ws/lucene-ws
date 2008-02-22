package net.lucenews3.http;

public class HttpConversationImpl implements HttpConversation {

	private HttpRequest request;
	private HttpResponse response;

	public HttpConversationImpl() {
		
	}

	public HttpConversationImpl(HttpRequest request, HttpResponse response) {
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
