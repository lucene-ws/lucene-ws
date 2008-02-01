package net.lucenews.http;

public class DefaultHttpResponse extends DefaultHttpCommunication implements HttpResponse {

	private Integer status;
	private String message;
	
	@Override
	public Integer getStatus() {
		return status;
	}

	@Override
	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public void setMessage(String message) {
		this.message = message;
	}

}
