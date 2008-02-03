package net.lucenews3.exception;

public class WebServiceException extends RuntimeException {

	private static final long serialVersionUID = -3658774847127565013L;

	public WebServiceException() {
		super();
	}
	
	public WebServiceException(String message) {
		super(message);
	}
	
	public WebServiceException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public WebServiceException(Throwable cause) {
		super(cause);
	}
	
}
