package net.lucenews3.controller;

import net.lucenews3.Service;

import org.springframework.web.servlet.mvc.Controller;

public abstract class AbstractController implements Controller {

	protected Service service;
	
	public Service getService() {
		return service;
	}
	
	public void setService(Service service) {
		this.service = service;
	}
	
}
