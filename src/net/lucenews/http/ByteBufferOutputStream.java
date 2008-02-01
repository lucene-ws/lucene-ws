package net.lucenews.http;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class ByteBufferOutputStream extends OutputStream {

	private ByteBuffer buffer;

	public ByteBufferOutputStream(ByteBuffer buffer) {
		this.buffer = buffer;
	}
	
	@Override
	public void write(int value) throws IOException {
		buffer.put((byte) value);
	}

}
