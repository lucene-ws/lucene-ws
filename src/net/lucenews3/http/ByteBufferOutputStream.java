package net.lucenews3.http;

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
		System.err.write(value);
		System.err.flush();
		buffer.put((byte) value);
		//System.err.println("Wrote " + value + " (" + ((char) value) + ")");
	}

}
