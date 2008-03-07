package net.lucenews3.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Deque;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

public class ViewStaticResourceController implements HttpController {

	private Logger logger;
	private File staticRootDirectory;
	
	public ViewStaticResourceController() {
		this(new File(new File("war"), "static"));
	}
	
	public ViewStaticResourceController(File staticRootDirectory) {
		this.staticRootDirectory = staticRootDirectory;
		this.logger = Logger.getLogger(getClass());
	}

	@SuppressWarnings("unchecked")
	@Override
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Deque<String> path = (Deque<String>) request.getAttribute("resourcePath");
		
		if (logger.isDebugEnabled()) {
			logger.debug("Static resource path: " + path);
		}
		
		if (path.isEmpty()) {
			throw new Exception("No static path");
		}
		
		File file = staticRootDirectory;
		for (String p : path) {
			file = new File(file, p);
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("Static resource file: " + file + " (" + file.getAbsolutePath() + ")");
		}
		
		final File resourceFile = file;
		
		return new ModelAndView(new View(){

			@Override
			public String getContentType() {
				return null;
			}

			@SuppressWarnings("unchecked")
			@Override
			public void render(Map model, HttpServletRequest request,
					HttpServletResponse response) throws Exception {
				final InputStream inputStream = new FileInputStream(resourceFile);
				final OutputStream outputStream = response.getOutputStream();
				
				byte[] buffer = new byte[512];
				int count = 0;
				while ((count = inputStream.read(buffer)) > 0) {
					outputStream.write(buffer, 0, count);
				}
			}});
	}
	
}
