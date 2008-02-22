package net.lucenews3.http;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletOutputStream;

public class ServletOutputStreamImpl extends ServletOutputStream {

	private OutputStream outputStream;
	
	public ServletOutputStreamImpl(OutputStream outputStream) {
		this.outputStream = outputStream;
	}
	
	@Override
	public void write(int b) throws IOException {
		outputStream.write(b);
	}

}
