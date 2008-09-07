package net.lucenews3;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class AtomExceptionParser extends AtomParser {

	private List<StackTraceElement> stackTrace;
	private Pattern stackTracePattern;
	protected String exceptionClass;
	protected String exceptionMessage;

	public static void main(String... args) {
		AtomExceptionParser parser = new AtomExceptionParser(null);
		System.out.println(parser.parseStackTraceElement("net.lucenews.model.LuceneIndexManager.getIndex(LuceneIndexManager.java:39)"));
	}

	public AtomExceptionParser(XMLStreamReader xml) {
		super(xml);
		this.stackTracePattern = Pattern.compile("(\\w+(\\.\\w+)*)\\.(\\w+)\\((\\w+\\.\\w+)\\:(\\d+)\\)");
		//this.stackTracePattern = Pattern.compile("\\((\\w+\\.\\w+)\\:(\\d+)\\)");
	}

	@Override
	public void parseEntry() throws XMLStreamException {
		// TODO Auto-generated method stub
		super.parseEntry();
		// TODO Build exception object
	}

	@Override
	protected void parseEntryTitle() throws XMLStreamException {
		super.parseEntryTitle();
		exceptionClass = entryTitle;
	}

	@Override
	protected void parseEntrySummary() throws XMLStreamException {
		super.parseEntrySummary();
		exceptionMessage = entrySummary;
	}

	@Override
	protected void parseOrderedList() throws XMLStreamException {
		// TODO Auto-generated method stub
		stackTrace = new ArrayList<StackTraceElement>();
		super.parseOrderedList();
	}

	@Override
	protected void parseListItem() throws XMLStreamException {
		String text = xml.getElementText();
		StackTraceElement stackTraceElement = parseStackTraceElement(text);
		stackTrace.add(stackTraceElement);
	}

	protected StackTraceElement parseStackTraceElement(String string) {
		Matcher matcher = stackTracePattern.matcher(string);
		if (matcher.matches()) {
			String declaringClass = matcher.group(1);
			String methodName = matcher.group(3);
			String fileName = matcher.group(4);
			int lineNumber = Integer.parseInt(matcher.group(5));
			return new StackTraceElement(declaringClass, methodName, fileName, lineNumber);
		} else {
			throw new IllegalArgumentException("\"" + string + "\" cannot be parsed into a stack trace element");
		}
	}

}
