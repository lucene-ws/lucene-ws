package net.lucenews3;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;

public class RequestMethodHandlerMapping implements HandlerMapping {

	public static final String DEFAULT_METHOD_OVERRIDE_HEADER_NAME = "X-HTTP-Method-Override";

	private String methodOverrideHeaderName;
	private HandlerMapping deleteHandlerMapping;
	private HandlerMapping getHandlerMapping;
	private HandlerMapping headHandlerMapping;
	private HandlerMapping postHandlerMapping;
	private HandlerMapping putHandlerMapping;
	private HandlerMapping traceHandlerMapping;

	public RequestMethodHandlerMapping() {
		this.methodOverrideHeaderName = DEFAULT_METHOD_OVERRIDE_HEADER_NAME;
		this.deleteHandlerMapping = new NullHandlerMapping();
		this.getHandlerMapping    = new NullHandlerMapping();
		this.headHandlerMapping   = new NullHandlerMapping();
		this.postHandlerMapping   = new NullHandlerMapping();
		this.putHandlerMapping    = new NullHandlerMapping();
		this.traceHandlerMapping  = new NullHandlerMapping();
	}

	/**
	 * Determines the method which will be used to select a sub-mapping
	 * based on the given request. Default implementation simply returns
	 * the given request's method, but sub-classes are free to override
	 * this behavior.
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	protected String getMethod(HttpServletRequest request) throws Exception {
		String method = request.getHeader(methodOverrideHeaderName);
		if (method == null) {
			return request.getMethod();
		} else {
			return method;
		}
	}

	/**
	 * Delegates handler mapping to the appropriate sub-mapping
	 * based upon the given request's method, as determined by
	 * {@link #getMethod(HttpServletRequest)}.
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 * @see {@link #getMethod(HttpServletRequest)}
	 */
	@Override
	public HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
		String method = getMethod(request);
		
		if (method.equalsIgnoreCase("GET")) {
			return getHandlerMapping.getHandler(request);
		} else if (method.equalsIgnoreCase("POST")) {
			return postHandlerMapping.getHandler(request);
		} else if (method.equalsIgnoreCase("PUT")) {
			return putHandlerMapping.getHandler(request);
		} else if (method.equalsIgnoreCase("DELETE")) {
			return deleteHandlerMapping.getHandler(request);
		} else if (method.equalsIgnoreCase("HEAD")) {
			return headHandlerMapping.getHandler(request);
		} else if (method.equalsIgnoreCase("TRACE")) {
			return traceHandlerMapping.getHandler(request);
		} else {
			return null;
		}
	}

	public String getMethodOverrideHeaderName() {
		return methodOverrideHeaderName;
	}

	public void setMethodOverrideHeaderName(String methodOverrideHeaderName) {
		this.methodOverrideHeaderName = methodOverrideHeaderName;
	}

	public HandlerMapping getDeleteHandlerMapping() {
		return deleteHandlerMapping;
	}

	public void setDeleteHandlerMapping(HandlerMapping deleteHandlerMapping) {
		this.deleteHandlerMapping = deleteHandlerMapping;
	}

	public HandlerMapping getGetHandlerMapping() {
		return getHandlerMapping;
	}

	public void setGetHandlerMapping(HandlerMapping getHandlerMapping) {
		this.getHandlerMapping = getHandlerMapping;
	}

	public HandlerMapping getHeadHandlerMapping() {
		return headHandlerMapping;
	}

	public void setHeadHandlerMapping(HandlerMapping headHandlerMapping) {
		this.headHandlerMapping = headHandlerMapping;
	}

	public HandlerMapping getPostHandlerMapping() {
		return postHandlerMapping;
	}

	public void setPostHandlerMapping(HandlerMapping postHandlerMapping) {
		this.postHandlerMapping = postHandlerMapping;
	}

	public HandlerMapping getPutHandlerMapping() {
		return putHandlerMapping;
	}

	public void setPutHandlerMapping(HandlerMapping putHandlerMapping) {
		this.putHandlerMapping = putHandlerMapping;
	}

	public HandlerMapping getTraceHandlerMapping() {
		return traceHandlerMapping;
	}

	public void setTraceHandlerMapping(HandlerMapping traceHandlerMapping) {
		this.traceHandlerMapping = traceHandlerMapping;
	}

}
