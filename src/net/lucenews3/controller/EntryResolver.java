package net.lucenews3.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.lucenews3.atom.Entry;

public interface EntryResolver {

	public List<Entry> resolveEntries(HttpServletRequest request);
	
}
