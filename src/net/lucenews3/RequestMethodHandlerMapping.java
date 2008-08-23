package net.lucenews3;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;

public class RequestMethodHandlerMapping implements HandlerMapping {

	private HandlerMapping deleteHandlerMapping;
	private HandlerMapping getHandlerMapping;
	private HandlerMapping headHandlerMapping;
	private HandlerMapping postHandlerMapping;
	private HandlerMapping putHandlerMapping;
	private HandlerMapping traceHandlerMapping;

	public RequestMethodHandlerMapping() {
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
	 * @param req
	 * @return
	 * @throws Exception
	 */
	protected String getMethod(HttpServletRequest req) throws Exception {
		String method = req.getHeader("X-HTTP-Method-Override");
		if (method == null) {
			return req.getMethod();
		} else {
			return method;
		}
	}

	/**
	 * Delegates handler mapping to the appropriate sub-mapping
	 * based upon the given request's method, as determined by
	 * {@link #getMethod(HttpServletRequest)}.
	 * 
	 * @param req
	 * @return
	 * @throws Exception
	 * @see {@link #getMethod(HttpServletRequest)}
	 */
	@Override
	public HandlerExecutionChain getHandler(HttpServletRequest req) throws Exception {
		String method = getMethod(req);
		
		if (method.equalsIgnoreCase("get")) {
			return getHandlerMapping.getHandler(req);
		} else if (method.equalsIgnoreCase("post")) {
			return postHandlerMapping.getHandler(req);
		} else if (method.equalsIgnoreCase("put")) {
			return putHandlerMapping.getHandler(req);
		} else if (method.equalsIgnoreCase("delete")) {
			return deleteHandlerMapping.getHandler(req);
		} else if (method.equalsIgnoreCase("head")) {
			return headHandlerMapping.getHandler(req);
		} else if (method.equalsIgnoreCase("trace")) {
			return traceHandlerMapping.getHandler(req);
		} else {
			return null;
		}
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
