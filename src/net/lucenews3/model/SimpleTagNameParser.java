package net.lucenews3.model;

public class SimpleTagNameParser<I> implements Parser<I, String> {

	@Override
	public String parse(I input) {
		if (input == null) {
			return "value";
		} else {
			return input.getClass().getSimpleName();
		}
	}

}
