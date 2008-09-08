package net.lucenews3;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class AtomDateFormat {

	public static DateFormat getInstance() {
		// TODO
		return new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
	}

}
