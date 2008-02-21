package net.lucenews3.model;

/**
 * A generic interface describing an object capable of accepting input and
 * returning a Java class.
 * 
 * @param <T>
 *            The type of object whose class is desired. For example, if one
 *            expects this parser to produce a class representing objects which
 *            can be assigned to a DateFormat variable, the parser would
 *            implement <code>ClassParser&lt;java.text.DateFormat, I&gt;</code>.
 * @param <I>
 *            The type of input provided to the parser.
 */
public interface ClassParser<T, I> extends Parser<I, Class<T>> {

	@Override
	public Class<T> parse(I input);

}
