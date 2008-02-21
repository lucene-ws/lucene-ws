package net.lucenews3.http;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletInputStream;

public class DefaultServletInputStream extends ServletInputStream {

	private InputStream inputStream;
	
	public DefaultServletInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	
	@Override
	public int read() throws IOException {
		return inputStream.read();
	}

}
