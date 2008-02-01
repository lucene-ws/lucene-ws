package net.lucenews.http;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletOutputStream;

public class DefaultServletOutputStream extends ServletOutputStream {

	private OutputStream outputStream;
	
	public DefaultServletOutputStream(OutputStream outputStream) {
		this.outputStream = outputStream;
	}
	
	@Override
	public void write(int b) throws IOException {
		outputStream.write(b);
	}

}
