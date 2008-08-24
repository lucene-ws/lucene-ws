package net.lucenews3;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;

public class ControllerHandlerMapping implements HandlerMapping {

	private Object controller;

	@Override
	public HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
		return new HandlerExecutionChain(controller);
	}

	public Object getController() {
		return controller;
	}

	public void setController(Object controller) {
		this.controller = controller;
	}

}
