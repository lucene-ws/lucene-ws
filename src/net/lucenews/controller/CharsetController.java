/**
 * A class for encoding input streams and writing resulting data
 * to output streams.
 */

package net.lucenews.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.MalformedInputException;
import java.nio.charset.UnmappableCharacterException;
import java.nio.charset.UnsupportedCharsetException;

public class CharsetController extends Controller {

	/**
	 * Encodes the input stream using the encoder provided by the Charset
	 * represented by <code>charsetName</code> and writes the resulting data
	 * to the output stream.
	 * 
	 * @param charsetName
	 *            the charset providing the desired CharsetEncoder
	 * @param in
	 *            the input stream
	 * @param out
	 *            the output stream
	 * @throws IllegalCharsetNameException
	 * @throws UnsupportedCharsetException
	 * @throws UnsupportedOperationException
	 * @throws IOException
	 *             if an exception occurred while reading from and/or writing to
	 *             the streams
	 * @throws IllegalStateException
	 * @throws MalformedInputException
	 * @throws UnmappableCharacterException
	 * @throws CodingErrorAction.REPORT
	 * @throws CharacterCodingException
	 */

	public static void encode(String charsetName, InputStream in,
			OutputStream out) throws IllegalCharsetNameException,
			UnsupportedCharsetException, UnsupportedOperationException,
			IOException, IllegalStateException, MalformedInputException,
			UnmappableCharacterException, CharacterCodingException {
		encode(Charset.forName(charsetName), in, out);
	}

	/**
	 * Encodes the input stream using the encoder provided by the specified
	 * charset and writes the resulting data to the output stream.
	 * 
	 * @param charset
	 *            the charset providing the desired CharsetEncoder
	 * @param in
	 *            the input stream
	 * @param out
	 *            the output stream
	 * @throws UnsupportedOperationException
	 * @throws IOException
	 *             if an exception occurred while reading from and/or writing to
	 *             the streams
	 * @throws IllegalStateException
	 * @throws MalformedInputException
	 * @throws UnmappableCharacterException
	 * @throws CodingErrorAction.REPORT
	 * @throws CharacterCodingException
	 */

	public static void encode(Charset charset, InputStream in, OutputStream out)
			throws UnsupportedOperationException, IOException,
			IllegalStateException, MalformedInputException,
			UnmappableCharacterException, CharacterCodingException {
		encode(charset.newEncoder(), in, out);
	}

	/**
	 * Encodes the input stream using the specified charset encoder and writes
	 * the resulting data to the output stream.
	 * 
	 * @param encoder
	 *            the CharsetEncoder used to encode the input stream
	 * @param in
	 *            the input stream
	 * @param out
	 *            the output stream
	 * @throws IOException
	 * @throws IllegalStateException
	 * @throws MalformedInputException
	 * @throws UnmappableCharacterException
	 * @throws CodingErrorAction.REPORT
	 * @throws CharacterCodingException
	 */

	public static void encode(CharsetEncoder encoder, InputStream in,
			OutputStream out) throws IOException, IllegalStateException,
			MalformedInputException, UnmappableCharacterException,
			CharacterCodingException {
		StringBuilder stringBuilder = new StringBuilder();

		int inChar = in.read();
		while (inChar >= 0) {
			stringBuilder.append(inChar);
			inChar = in.read();
		}

		CharBuffer charBuffer = CharBuffer.wrap(stringBuilder);

		ByteBuffer byteBuffer = encoder.encode(charBuffer);

		try {
			while (true) {
				out.write(byteBuffer.get());
			}
		} catch (BufferUnderflowException bue) {
		}
	}

}
