package net.lucenews3.lucene.support;

/**
 * Generic interface for all objects capable of parsing input into output.
 * 
 * For example, a class capable of parsing a <code>Locale</code> from a
 * <code>String</code> would implement <code>Parser<String, Locale></code>.
 * 
 * @param <I>
 *            The class of input
 * @param <O>
 *            The class of output
 */
public interface Parser<I, O> {

	public O parse(I input);

}
