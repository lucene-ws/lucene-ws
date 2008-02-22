package net.lucenews3.http;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletInputStream;

public class ServletInputStreamImpl extends ServletInputStream {

	private InputStream inputStream;
	
	public ServletInputStreamImpl(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	
	@Override
	public int read() throws IOException {
		return inputStream.read();
	}

}
