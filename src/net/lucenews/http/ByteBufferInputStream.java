package net.lucenews.http;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class ByteBufferInputStream extends InputStream {

	private ByteBuffer buffer;
	
	public ByteBufferInputStream(ByteBuffer buffer) {
		this.buffer = buffer;
		int position = buffer.position();
		buffer.limit(position);
		buffer.position(0);
	}
	
	@Override
	public int read() throws IOException {
		int result;
		//System.out.println("LIMIT: " + buffer.limit() + ", POSITION: " + buffer.position());
		if (buffer.hasRemaining()) {
			result = buffer.get() & 0xFF;
			//System.out.write(result);
			//System.out.flush();
		} else {
			result = -1;
		}
		//System.err.println("Read " + result + " (" + ((char) result) + ")");
		return result;
	}

}
