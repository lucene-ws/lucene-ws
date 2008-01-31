package net.lucenews.view;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import net.lucenews.LuceneContext;
import net.lucenews.atom.Entry;
import net.lucenews.atom.Feed;
import net.lucenews.atom.IntrospectionDocument;

public class AtomView extends View {

	private static final long serialVersionUID = 3709227611422920014L;

	public static void process(LuceneContext c)
			throws ParserConfigurationException, TransformerException,
			IOException {
		if (c.getStash().get("atom_introspection_document") instanceof IntrospectionDocument) {
			process(c, (IntrospectionDocument) c.getStash().get(
					"atom_introspection_document"));
		}

		if (c.getStash().get("atom_feed") instanceof Feed) {
			process(c, (Feed) c.getStash().get("atom_feed"));
		}
	}

	public static void process(LuceneContext c,
			IntrospectionDocument introspectionDocument)
			throws ParserConfigurationException, TransformerException,
			IOException {
		c.getResponse().setContentType("application/atomsvc+xml;charset=utf-8");
		XMLView.process(c, introspectionDocument.asDocument());
	}

	public static void process(LuceneContext c, Feed feed)
			throws ParserConfigurationException, TransformerException,
			IOException {
		c.getResponse().setContentType("application/atom+xml;charset=utf-8");
		XMLView.process(c, feed.asDocument());
	}

	public static void process(LuceneContext c, Entry entry)
			throws ParserConfigurationException, TransformerException,
			IOException {
		c.getResponse().setContentType("application/atom+xml;charset=utf-8");
		XMLView.process(c, entry.asDocument());
	}

}
