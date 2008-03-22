package net.lucenews3.model;

import java.util.Collection;

public interface SynonymSource {

	public Collection<String> getSynonyms(String word);
	
}
