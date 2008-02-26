package net.lucenews3.model;

import org.apache.lucene.queryParser.Token;

public interface TokenSource {

	public Token getToken();
	
	public void setToken(Token token);
	
}
