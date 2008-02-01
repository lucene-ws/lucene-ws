package net.lucenews.http;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class ByteBufferInputStream extends InputStream {

	private ByteBuffer buffer;
	
	public ByteBufferInputStream(ByteBuffer buffer) {
		this.buffer = buffer;
	}
	
	@Override
	public int read() throws IOException {
		int result;
		if (buffer.hasRemaining()) {
			result = buffer.get() & 0xFF;
		} else {
			result = -1;
		}
		return result;
	}

}
