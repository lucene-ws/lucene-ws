package net.lucenews3;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.handler.AbstractHandlerMapping;

public class NullHandlerMapping extends AbstractHandlerMapping {

	@Override
	protected Object getHandlerInternal(HttpServletRequest req) throws Exception {
		return null;
	}

}
