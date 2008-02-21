package net.lucenews3.model;

import java.util.ArrayList;
import java.util.List;

import net.lucenews3.atom.Entry;
import net.lucenews3.atom.Feed;

/**
 * Parses a list of Atom entries from input using the associated
 * feed parser and/or entry parser for that particular input.
 *
 * @param <I>
 */
public class EntryListParserImpl<I> implements EntryListParser<I> {

	private FeedParser<I> feedParser;
	private EntryParser<I> entryParser;
	
	@Override
	public List<Entry> parse(I input) {
		try {
			return parse(feedParser.parse(input));
		} catch (ParseException e) {
			// Don't worry, try parsing an entry
		}
		
		try {
			return parse(entryParser.parse(input));
		} catch (ParseException e) {
			// Trouble
		}
		
		throw new ParseException("Cannot find any entries");
	}
	
	public List<Entry> parse(Feed feed) {
		return feed.getEntries();
	}

	public List<Entry> parse(Entry entry) {
		List<Entry> result = new ArrayList<Entry>(1);
		result.add(entry);
		return result;
	}
	
	public FeedParser<I> getFeedParser() {
		return feedParser;
	}

	public void setFeedParser(FeedParser<I> feedParser) {
		this.feedParser = feedParser;
	}

	public EntryParser<I> getEntryParser() {
		return entryParser;
	}

	public void setEntryParser(EntryParser<I> entryParser) {
		this.entryParser = entryParser;
	}

}
